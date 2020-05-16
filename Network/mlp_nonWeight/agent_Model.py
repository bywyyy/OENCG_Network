import torch 
import torch.nn as nn
import xlrd
import time
import xlwt
from xlutils.copy import copy

timen = time.strftime("%m%d%H%M")
dataPath = '../saveData/mlp_nw' + timen + '.xls'

class LinearNet(nn.Module):
    def __init__(self):
        super(LinearNet, self).__init__()


        w = xlwt.Workbook(encoding='utf-8')  # 新建工作簿
        ws = w.add_sheet('data')  # 新建sheet
        w.save(dataPath)
        workbook = xlrd.open_workbook(dataPath)  # 打开工作簿
        sheets = workbook.sheet_names()  # 获取工作簿中的所有表格
        worksheet = workbook.sheet_by_name(sheets[0])  # 获取工作簿中所有表格中的的第一个表格
        rows_old = worksheet.nrows  # 获取表格中已存在的数据的行数
        new_workbook = copy(workbook)  # 将xlrd对象拷贝转化为xlwt对象
        new_worksheet = new_workbook.get_sheet(0)  # 获取转化后工作簿中的第一个表格

        fc_list = [18, 28, 46, 28, 16]
        new_worksheet.write(rows_old, 0, format(fc_list))

        seq_list = []
        for i in range(len(fc_list) - 1):
            seq_list.append(nn.Linear(fc_list[i], fc_list[i + 1]))
            seq_list.append(nn.ReLU())
        self.feature = nn.Sequential(*seq_list)


        fc_list = [16, 16, 8, 3]
        seq_list = []
        for i in range(len(fc_list) - 1):
            seq_list.append(nn.Linear(fc_list[i], fc_list[i + 1]))
            seq_list.append(nn.ReLU())
        self.union_predict = nn.Sequential(*seq_list)
        new_worksheet.write(rows_old, 1, format(fc_list))


        fc_list = [16, 12, 8, 3]
        seq_list = []
        for i in range(len(fc_list) - 1):
            seq_list.append(nn.Linear(fc_list[i], fc_list[i + 1]))
            seq_list.append(nn.ReLU())
        self.score_predict = nn.Sequential(*seq_list)
        new_worksheet.write(rows_old, 2, format(fc_list))

        new_workbook.save(dataPath)
    
    
    def forward(self, x):
        feature_map = self.feature(x)
        score = self.score_predict(feature_map)
        union = self.union_predict(feature_map)
        output = torch.cat((score, union), dim=1)
        return output