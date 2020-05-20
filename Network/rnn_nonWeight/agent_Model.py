import time
import torch.nn as nn
import torch.nn.functional as F
import xlrd
import xlwt
from xlutils.copy import copy

timen = time.strftime("%m%d%H%M")
dataPath = '../saveData/rnn_nw' + timen + '.xls'


class RNNnet(nn.Module):
    def __init__(self, input_size, hidden_size, output_size=3, num_layers=2):
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
        new_worksheet.write(rows_old, 0, 'LeakyReLU,α=0.0001')

        setList = [input_size, hidden_size, output_size, num_layers]
        setData = "input_size={0[0]},hidden_size = {0[1]},output_size = {0[2]},num_layers={0[3]}".format(setList)
        new_worksheet.write(rows_old, 1, setData)
        self.rnn = nn.RNN(input_size, hidden_size, num_layers)
        self.reg = nn.Linear(hidden_size, output_size)

        new_workbook.save(dataPath)

    def forward(self, x):
        x, _ = self.rnn(x)  # 未在不同序列中传递hidden_state
        return self.reg(x)
