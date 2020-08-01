import time
import torch.nn as nn
import torch.nn.functional as F
import math

timen = time.strftime("%m%d%H%M%S")
dataPath = '../saveData/cnn_nw' + timen + '.xls'

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

        in_channels = 3
        out_channels = 16
        kernel_size = 2
        stride = 1
        padding = 0
        self.conv1 = nn.Sequential(
            nn.Conv1d(in_channels=in_channels,
                      out_channels=out_channels,
                      kernel_size=kernel_size,
                      stride=stride,
                      padding=padding),
            nn.BatchNorm1d(16),
            nn.ReLU()
        )

        in_channels = 16
        out_channels = 32
        kernel_size = 1
        stride = 1
        padding = 0

        self.conv2 = nn.Sequential(
            nn.Conv1d(in_channels, out_channels, kernel_size, stride, padding),
            nn.BatchNorm1d(32),
            nn.ReLU()
        )
        self.conv3 = nn.Sequential(
            nn.Conv1d(32, 64, 1, 2, 0),
            nn.BatchNorm1d(64),
            nn.ReLU()
        )

        # an affine operarion: y = Wx + b
        # 全连接层fc1
        in_features = num
        out_features = int(num / 2)  # 64
        self.fc1 = nn.Linear(in_features=in_features, out_features=out_features)
        out_features = int(num / 2)  # 64
        out_features = int(num / 8)  # 64
        self.fc2 = nn.Linear(in_features=in_features, out_features=out_features)
        out_features = int(num / 8)  # 64
        out_features = int(num / 64)  # 64
        self.fc3 = nn.Linear(in_features=in_features, out_features=out_features)
        out_features = int(num / 64)  # 64
        out_features = 2
        self.fc4 = nn.Linear(in_features=in_features, out_features=out_features)

        self.dropout = nn.Dropout(p=0.4)


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
