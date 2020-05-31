import time

import numpy as np
import matplotlib.pyplot as plt
import torch
import torch.nn as nn
import random
from torch.autograd import Variable
import xlrd
from xlutils.copy import copy

plt.ion()
colorlist = ['b', 'g', 'r', 'm', 'y', 'k']
epoch_list = []
accuracy_list = []


def _train(model, dl, num_epochs, learning_rate):
    plt.figure('cnn_nw')
    # optimizer = torch.optim.Adam(model.parameters(), lr=learning_rate, weight_decay=0.01)

    optimizer = torch.optim.Adam(model.parameters(), lr=learning_rate)
    criterion = nn.CrossEntropyLoss()

    for epoch in range(num_epochs):
        if (epoch + 1) % 10 == 0:
            epoch_list.append(epoch + 1)
        i = 0
        total_cnt = 0
        loss_sum = 0
        for (item_data, item_label) in dl:
            batch_x = Variable(torch.unsqueeze(item_data, dim=1).float(), requires_grad=False)
            # batch_y = Variable(torch.unsqueeze(item_label, dim=1).float(), requires_grad=False)

            outputs = model(batch_x)
            optimizer.zero_grad()
            labelloss = item_label.squeeze(1).long()
            loss = criterion(outputs, labelloss)
            loss_sum += loss
            loss.backward()
            optimizer.step()

            total_cnt += 1
            i += 1
        if (epoch + 1) % 1 == 0:
            avgloss = loss_sum.item() / total_cnt
            print('Epoch [{}/{}], Setp [{}], LOSS: {:.4f}, Total Sample {}'
                  .format(epoch + 1, num_epochs, i, avgloss, total_cnt))
            from agent_Model import dataPath
            workbook = xlrd.open_workbook(dataPath)  # 打开工作簿
            sheets = workbook.sheet_names()  # 获取工作簿中的所有表格
            worksheet = workbook.sheet_by_name(sheets[0])  # 获取工作簿中所有表格中的的第一个表格
            rows_old = worksheet.nrows  # 获取表格中已存在的数据的行数
            new_workbook = copy(workbook)  # 将xlrd对象拷贝转化为xlwt对象
            new_worksheet = new_workbook.get_sheet(0)  # 获取转化后工作簿中的第一个表格
            new_worksheet.write(rows_old, 0, epoch + 1)
            new_worksheet.write(rows_old, 1, i)
            new_worksheet.write(rows_old, 2, avgloss)
            new_worksheet.write(rows_old, 3, total_cnt)
            new_workbook.save(dataPath)
            yield
            model.train()


def _test(model, dst):
    model.eval()
    accuracy = 0.0
    # for i in range(0, 50):
    for i in range(0, dst.__len__()):
        # rand_idx = random.randint(0, dst.__len__() - 1)
        # data, label = dst[rand_idx]
        data, label = dst[i]
        data = torch.Tensor([data.numpy()])
        data = Variable(torch.unsqueeze(data, dim=1).float(), requires_grad=False)
        outputs = model(data).squeeze(0)
        outputs2 = outputs.detach().numpy()
        np.set_printoptions(precision=4, suppress=True)

        payoff = data.squeeze(0).squeeze(0).numpy()
        payoff2 = payoff[-3:]
        payoff3 = list(map(int, payoff2 * 100))

        if outputs2[int(label[0])] > 0.5:
            accuracy += 1

        accuracyRate = accuracy / (i + 1)
    print("payoff: {}, ground truth: {},  outputs : {}, accuracy : {:.4f}".format(payoff3, label, outputs2,
                                                                                  accuracyRate))
    from agent_Model import dataPath
    workbook = xlrd.open_workbook(dataPath)  # 打开工作簿
    sheets = workbook.sheet_names()  # 获取工作簿中的所有表格
    worksheet = workbook.sheet_by_name(sheets[0])  # 获取工作簿中所有表格中的的第一个表格
    rows_old = worksheet.nrows  # 获取表格中已存在的数据的行数
    new_workbook = copy(workbook)  # 将xlrd对象拷贝转化为xlwt对象
    new_worksheet = new_workbook.get_sheet(0)  # 获取转化后工作簿中的第一个表格
    new_worksheet.write(rows_old - 1, 4, format(payoff3))
    new_worksheet.write(rows_old - 1, 5, int(label[0]))
    new_worksheet.write(rows_old - 1, 6, format(outputs2))
    new_worksheet.write(rows_old - 1, 7, accuracyRate)
    new_workbook.save(dataPath)

    accuracy_list.append(accuracyRate)

    randomNumber = random.randint(0, 5)
    color = colorlist[randomNumber]
    plt.plot(epoch_list, accuracy_list, c=color, ls='-', marker='o', mec='b', mfc='w')  ## 保存历史数据
    plt.ylim((0, 1))
    plt.pause(0.3)
    if epoch_list.__len__() == 20:
        timen = time.strftime("%m%d%H%M%S")
        from main import globalk
        plt.savefig('cnn_nw' + timen + 'k' + globalk.__str__() + '.png')
        epoch_list.clear()
        accuracy_list.clear()
        plt.close()
