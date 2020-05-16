import torch
import torch.nn as nn
import random
import xlrd
from xlutils.copy import copy
from agent_Model import dataPath


def _train(model, dl, num_epochs, learning_rate):
    # optimizer = torch.optim.Adam(model.parameters(), lr=learning_rate, weight_decay=0.01)

    optimizer = torch.optim.Adam(model.parameters(), lr=learning_rate)
    criterion = nn.MSELoss()

    for epoch in range(num_epochs):
        i = 0
        cur_cnt = 0
        total_cnt = 0
        for (item_data, item_label) in dl:
            outputs = model(item_data)

            optimizer.zero_grad()

            loss = criterion(outputs, item_label)
            loss.backward()
            optimizer.step()

            total_cnt += 1
            i += 1
        if (epoch + 1) % 1 == 0:
            print('Epoch [{}/{}], Setp [{}], LOSS: {:.4f}, Total Sample {}'
                  .format(epoch + 1, num_epochs, i, loss.item(), total_cnt))
            workbook = xlrd.open_workbook(dataPath)  # 打开工作簿
            sheets = workbook.sheet_names()  # 获取工作簿中的所有表格
            worksheet = workbook.sheet_by_name(sheets[0])  # 获取工作簿中所有表格中的的第一个表格
            rows_old = worksheet.nrows  # 获取表格中已存在的数据的行数
            new_workbook = copy(workbook)  # 将xlrd对象拷贝转化为xlwt对象
            new_worksheet = new_workbook.get_sheet(0)  # 获取转化后工作簿中的第一个表格
            new_worksheet.write(rows_old, 0, epoch + 1)
            new_worksheet.write(rows_old, 1, i)
            new_worksheet.write(rows_old, 2, loss.item())
            new_worksheet.write(rows_old, 3, total_cnt)
            new_workbook.save(dataPath)

            yield
            model.train()


def _test(model, dst):
    model.eval()

    rand_idx = random.randint(0, dst.__len__())
    data, label = dst[rand_idx]

    data = torch.Tensor([data.numpy()])
    outputs = model(data)

    '''
    outputs = outputs[0]
    sum_data = outputs[0] + outputs[1] + outputs[2]

    outputs[0] = outputs[0] / sum_data * 100
    outputs[1] = outputs[1] / sum_data * 100
    outputs[2] = outputs[2] / sum_data * 100
    '''

    print("ground truth: {},  outputs : {}".format(label, outputs[0]))
    workbook = xlrd.open_workbook(dataPath)  # 打开工作簿
    sheets = workbook.sheet_names()  # 获取工作簿中的所有表格
    worksheet = workbook.sheet_by_name(sheets[0])  # 获取工作簿中所有表格中的的第一个表格
    rows_old = worksheet.nrows  # 获取表格中已存在的数据的行数
    new_workbook = copy(workbook)  # 将xlrd对象拷贝转化为xlwt对象
    new_worksheet = new_workbook.get_sheet(0)  # 获取转化后工作簿中的第一个表格
    new_worksheet.write(rows_old-1, 4, format(label))
    new_worksheet.write(rows_old-1, 5, format(outputs[0]))
    new_workbook.save(dataPath)
