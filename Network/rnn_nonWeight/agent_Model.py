import time

import torch
import torch.nn as nn
import torch.nn.functional as F
import xlrd
import xlwt
from xlutils.copy import copy

timen = time.strftime("%m%d%H%M")
dataPath = '../saveData/rnn_nw' + timen + '.xls'


class RNNnet(nn.Module):
    def __init__(self, input_size, hidden_size, output_size, num_layers):
        super(RNNnet, self).__init__()

        w = xlwt.Workbook(encoding='utf-8')  # 新建工作簿
        ws = w.add_sheet('data')  # 新建sheet
        w.save(dataPath)
        workbook = xlrd.open_workbook(dataPath)  # 打开工作簿
        sheets = workbook.sheet_names()  # 获取工作簿中的所有表格
        worksheet = workbook.sheet_by_name(sheets[0])  # 获取工作簿中所有表格中的的第一个表格
        rows_old = worksheet.nrows  # 获取表格中已存在的数据的行数
        new_workbook = copy(workbook)  # 将xlrd对象拷贝转化为xlwt对象
        new_worksheet = new_workbook.get_sheet(0)  # 获取转化后工作簿中的第一个表格
        # new_worksheet.write(rows_old, 0, 'LeakyReLU,α=0.0001')

        setList = [input_size, hidden_size, output_size, num_layers]
        setData = "input_size={0[0]},hidden_size = {0[1]},output_size = {0[2]},num_layers={0[3]}".format(setList)
        new_worksheet.write(rows_old, 1, setData)
        self.hidden_size = hidden_size
        self.num_layers = num_layers
        self.lstm = nn.LSTM(input_size, hidden_size, num_layers, batch_first=True)
        self.reg = nn.Linear(hidden_size, output_size)
        # self.f1 = nn.Sequential(nn.Linear(hidden_size, 128),
        #                         nn.Dropout(0.8),
        #                         nn.ReLU())
        # self.f2 = nn.Sequential(nn.Linear(128, output_size),
        #                         nn.Softmax())



        new_workbook.save(dataPath)

    def forward(self, x):
        h0 = torch.zeros(self.num_layers, x.size(0), self.hidden_size)  # x.size(0)是batch_size
        c0 = torch.zeros(self.num_layers, x.size(0), self.hidden_size)
        x, _ = self.lstm(x, (h0, c0))  # 未在不同序列中传递hidden_state
        # x = F.dropout(x, p=0.8)
        x = self.reg(x[:, -1, :])
        # x=self.f2(x)
        return x
