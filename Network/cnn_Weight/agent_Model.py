import time
import torch.nn as nn
import torch.nn.functional as F
import xlrd
import xlwt
from xlutils.copy import copy

timen = time.strftime("%m%d%H%M")
dataPath = '../saveData/cnn_w' + timen + '.xls'


class CNNnet(nn.Module):
    def __init__(self):
        super(CNNnet, self).__init__()

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


        in_channels = 1
        out_channels = 16
        kernel_size = 3
        stride = 2
        padding = 1
        # conv1层，in_channels=2 , out_channels=6 说明使用了6个滤波器/卷积核
        # kernel_size=5卷积核大小5
        setList = [in_channels, out_channels, kernel_size, stride, padding]
        setData = "in_channels={0[0]},out_channels = {0[1]},kernel_size = {0[2]},stride = {0[3]},padding = {0[4]}".format(setList)
        new_worksheet.write(rows_old, 1, setData)
        self.conv1 = nn.Conv1d(in_channels=in_channels,
                               out_channels=out_channels,
                               kernel_size=kernel_size,
                               stride=stride,
                               padding=padding)

        in_channels = 16
        out_channels = 16
        kernel_size = 2
        stride = 2
        padding = 0
        setList = [in_channels, out_channels, kernel_size, stride, padding]
        setData = "in_channels={0[0]},out_channels = {0[1]},kernel_size = {0[2]},stride = {0[3]},padding = {0[4]}".format(setList)
        new_worksheet.write(rows_old, 2, setData)
        # conv2层， 输入通道in_channels 要等于上一层的 out_channels
        self.conv2 = nn.Conv1d(in_channels=in_channels,
                               out_channels=out_channels,
                               kernel_size=kernel_size,
                               stride=stride,
                               padding=padding)
        # an affine operarion: y = Wx + b
        # 全连接层fc1,因为输入到fc1层时候，feature map为： 2*2*10
        in_features = 80
        out_features = 64
        setList = [in_features, out_features]
        setData = "in_features={0[0]},out_features = {0[1]}".format(setList)
        new_worksheet.write(rows_old, 3, setData)
        self.fc1 = nn.Linear(in_features=in_features, out_features=out_features)
        in_features = 64
        out_features = 24
        setList = [in_features, out_features]
        setData = "in_features={0[0]},out_features = {0[1]}".format(setList)
        new_worksheet.write(rows_old, 4, setData)
        self.fc2 = nn.Linear(in_features=in_features, out_features=out_features)
        in_features = 24
        out_features = 3
        setList = [in_features, out_features]
        setData = "in_features={0[0]},out_features = {0[1]}".format(setList)
        new_worksheet.write(rows_old, 5, setData)
        self.fc3 = nn.Linear(in_features=in_features, out_features=out_features)

        new_workbook.save(dataPath)

    def forward(self, x):
        # # Max pooling over a (2, 2) window
        # x = F.max_pool2d(F.relu(self.conv1(x)), (2, 2))
        # # If the size is a square you can only specify a single number
        # x = F.max_pool2d(F.relu(self.conv2(x)), 2)
        # # 特征图转换为一个１维的向量
        # x = x.view(-1, self.num_flat_features(x))
        # x = Variable(torch.unsqueeze(x, dim=0).float(), requires_grad=False)
        x = self.conv1(x)
        x = self.conv2(x)
        x = x.view(x.size(0), -1)
        x = F.leaky_relu(self.fc1(x))
        x = F.leaky_relu(self.fc2(x))
        x = self.fc3(x)

        return x
