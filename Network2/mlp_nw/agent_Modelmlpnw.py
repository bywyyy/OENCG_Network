import torch.nn.functional as F
import torch.nn as nn
import xlrd
import time
import xlwt
from xlutils.copy import copy

timen = time.strftime("%m%d%H%M%S")
dataPath = '../saveData/mlp_nw' + timen + '.xls'

num = 0


def setnum(int):
    global num
    num = int


class LinearNet(nn.Module):
    def __init__(self):
        super(LinearNet, self).__init__()

        w = xlwt.Workbook(encoding='utf-8')  # 新建工作簿
        ws = w.add_sheet('data')  # 新建sheet
        global timen
        timen = time.strftime("%m%d%H%M%S")
        from main import globalk
        global dataPath
        dataPath = '../saveData/mlp_nw' + timen + 'k' + globalk.__str__() + '.xls'
        w.save(dataPath)
        workbook = xlrd.open_workbook(dataPath)  # 打开工作簿
        sheets = workbook.sheet_names()  # 获取工作簿中的所有表格
        worksheet = workbook.sheet_by_name(sheets[0])  # 获取工作簿中所有表格中的的第一个表格
        rows_old = worksheet.nrows  # 获取表格中已存在的数据的行数
        new_workbook = copy(workbook)  # 将xlrd对象拷贝转化为xlwt对象
        new_worksheet = new_workbook.get_sheet(0)  # 获取转化后工作簿中的第一个表格
        new_worksheet.write(rows_old, 0, 'LeakyReLU,α=0.002')

        inputNum = num * 9 - 3
        fc_list = [inputNum, inputNum, int(inputNum / 2)]
        new_worksheet.write(rows_old, 1, format(fc_list))

        seq_list = []
        for i in range(len(fc_list) - 1):
            seq_list.append(nn.Linear(fc_list[i], fc_list[i + 1]))
            seq_list.append(nn.Dropout(0.4))
            seq_list.append(nn.LeakyReLU())
        self.feature = nn.Sequential(*seq_list)

        # fc_list = [12, 12, 8, 3]
        # seq_list = []
        # for i in range(len(fc_list) - 1):
        #     seq_list.append(nn.Linear(fc_list[i], fc_list[i + 1]))
        #     seq_list.append(nn.LeakyReLU())
        # self.union_predict = nn.Sequential(*seq_list)
        # new_worksheet.write(rows_old, 2, format(fc_list))

        fc_list = [int(inputNum / 2), int(inputNum / 8), 2]
        seq_list = []
        for i in range(len(fc_list) - 1):
            seq_list.append(nn.Linear(fc_list[i], fc_list[i + 1]))
            seq_list.append(nn.Dropout(0.4))
            seq_list.append(nn.LeakyReLU())
        self.prob_predict = nn.Sequential(*seq_list)
        new_worksheet.write(rows_old, 2, format(fc_list))

        new_workbook.save(dataPath)

    def forward(self, x):
        feature_map = self.feature(x)
        score = self.prob_predict(feature_map)
        # union = self.union_predict(feature_map)
        # output = torch.cat((score, union), dim=1)
        # output = torch.cat((score), dim=1)
        output = F.softmax(score, dim=1)
        return output
