import time
import torch.nn as nn
import torch.nn.functional as F
import xlrd
import xlwt
from xlutils.copy import copy

timen = time.strftime("%m%d%H%M")
dataPath = '../saveData/cnn_w' + timen + '.xls'

num = 0
globalk = 0


def setnum(int):
    global num
    num = int


def setk(int):
    global globalk
    globalk = int


class CNNnet(nn.Module):
    def __init__(self):
        super(CNNnet, self).__init__()

        w = xlwt.Workbook(encoding='utf-8')  # 新建工作簿
        ws = w.add_sheet('data')  # 新建sheet
        global timen
        timen = time.strftime("%m%d%H%M")
        from main import globalk
        global dataPath
        dataPath = '../saveData/cnn_w' + timen + 'k' + globalk.__str__() + '.xls'
        w.save(dataPath)
        workbook = xlrd.open_workbook(dataPath)  # 打开工作簿
        sheets = workbook.sheet_names()  # 获取工作簿中的所有表格
        worksheet = workbook.sheet_by_name(sheets[0])  # 获取工作簿中所有表格中的的第一个表格
        rows_old = worksheet.nrows  # 获取表格中已存在的数据的行数
        new_workbook = copy(workbook)  # 将xlrd对象拷贝转化为xlwt对象
        new_worksheet = new_workbook.get_sheet(0)  # 获取转化后工作簿中的第一个表格
        new_worksheet.write(rows_old, 0, 'LeakyReLU,α=0.00001')

        in_channels = 3
        out_channels = 16
        kernel_size = 2
        stride = 1
        padding = 0
        setList = [in_channels, out_channels, kernel_size, stride, padding]
        setData = "in_channels={0[0]},out_channels = {0[1]},kernel_size = {0[2]},stride = {0[3]},padding = {0[4]}".format(
            setList)
        new_worksheet.write(rows_old, 1, setData)
        self.conv1 = nn.Sequential(
            nn.Conv1d(in_channels=in_channels,
                      out_channels=out_channels,
                      kernel_size=kernel_size,
                      stride=stride,
                      padding=padding),
            nn.BatchNorm1d(16),
            nn.ReLU()
        )

        # self.conv1 = nn.Conv2d(in_channels=in_channels,
        #                        out_channels=out_channels,
        #                        kernel_size=kernel_size,
        #                        stride=stride,
        #                        padding=padding)

        in_channels = 16
        out_channels = 32
        kernel_size = 1
        stride = 1
        padding = 0
        setList = [in_channels, out_channels, kernel_size, stride, padding]
        setData = "in_channels={0[0]},out_channels = {0[1]},kernel_size = {0[2]},stride = {0[3]},padding = {0[4]}".format(
            setList)
        new_worksheet.write(rows_old, 2, setData)
        # # conv2层， 输入通道in_channels 要等于上一层的 out_channels
        # self.conv2 = nn.Conv1d(in_channels=in_channels,
        #                        out_channels=out_channels,
        #                        kernel_size=kernel_size,
        #                        stride=stride,
        #                        padding=padding)
        self.conv2 = nn.Sequential(
            nn.Conv1d(in_channels, out_channels, kernel_size, stride, padding),
            nn.BatchNorm1d(32),
            nn.ReLU()
        )
        self.conv3 = nn.Sequential(
            nn.Conv1d(32, 64, 2, 2, 0),
            nn.BatchNorm1d(64),
            nn.ReLU()
        )

        # an affine operarion: y = Wx + b
        # 全连接层fc1
        in_features = num
        out_features = int(num / 2)  # 64
        setList = [in_features, out_features]
        setData = "in_features={0[0]},out_features = {0[1]}".format(setList)
        new_worksheet.write(rows_old, 3, setData)
        self.fc1 = nn.Linear(in_features=in_features, out_features=out_features)
        in_features = int(num / 2)  # 64
        out_features = int(num / 4)  # 24
        setList = [in_features, out_features]
        setData = "in_features={0[0]},out_features = {0[1]}".format(setList)
        new_worksheet.write(rows_old, 4, setData)
        self.fc2 = nn.Linear(in_features=in_features, out_features=out_features)
        in_features = int(num / 4)  # 24
        out_features = int(num / 8)
        setList = [in_features, out_features]
        setData = "in_features={0[0]},out_features = {0[1]}".format(setList)
        new_worksheet.write(rows_old, 5, setData)
        self.fc3 = nn.Linear(in_features=in_features, out_features=out_features)
        in_features = int(num / 8)
        out_features = 2
        setList = [in_features, out_features]
        setData = "in_features={0[0]},out_features = {0[1]}".format(setList)
        new_worksheet.write(rows_old, 6, setData)
        self.fc4 = nn.Linear(in_features=in_features, out_features=out_features)

        self.dropout = nn.Dropout(p=0.4)

        new_workbook.save(dataPath)

    def forward(self, x):
        x = self.conv1(x)
        x = self.conv2(x)
        x = self.conv3(x)
        x = x.view(x.size(0), -1)
        x = F.leaky_relu(self.fc1(x))
        x = self.dropout(x)
        x = F.leaky_relu(self.fc2(x))
        x = self.dropout(x)
        x = F.leaky_relu(self.fc3(x))
        x = self.dropout(x)
        x_prob = F.softmax(self.fc4(x), dim=1)

        return x_prob
