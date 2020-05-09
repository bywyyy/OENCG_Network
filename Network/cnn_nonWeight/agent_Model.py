import torch
import torch.nn as nn
import torch.nn.functional as F
from torch.autograd import Variable


class CNNnet(nn.Module):
    def __init__(self):
        super(CNNnet, self).__init__()

        #     #  conv1层，in_channels=1, out_channels=6 说明使用了6个滤波器/卷积核，
        #     # kernel_size=5卷积核大小5
        #     self.conv1 = nn.Conv2d(in_channels=1, out_channels=6, kernel_size=2)
        #     # conv2层， 输入通道in_channels 要等于上一层的 out_channels
        #     self.conv2 = nn.Conv2d(in_channels=6, out_channels=10, kernel_size=2)
        #     # an affine operarion: y = Wx + b
        #     # 全连接层fc1,因为输入到fc1层时候，feature map为： 2*2*10
        #     self.fc1 = nn.Linear(10 * 2 * 2, 120)
        #     self.fc2 = nn.Linear(in_features=120, out_features=64)
        #     self.fc3 = nn.Linear(in_features=64, out_features=3)
        #
        #
        # def forward(self, x):
        #     # # Max pooling over a (2, 2) window
        #     # x = F.max_pool2d(F.relu(self.conv1(x)), (2, 2))
        #     # # If the size is a square you can only specify a single number
        #     # x = F.max_pool2d(F.relu(self.conv2(x)), 2)
        #     # # 特征图转换为一个１维的向量
        #     # x = x.view(-1, self.num_flat_features(x))
        #     # x = Variable(torch.unsqueeze(x, dim=0).float(), requires_grad=False)
        #     x = self.conv1(x)
        #     x = self.conv2(x)
        #     x = x.view(x.size(0), -1)
        #     x = F.relu(self.fc1(x))
        #     x = F.relu(self.fc2(x))
        #     x = self.fc3(x)
        #
        #     return x
        self.conv1 = torch.nn.Sequential(
            torch.nn.Conv2d(in_channels=1,
                            out_channels=16,
                            kernel_size=3,
                            stride=2,
                            padding=1),
            torch.nn.BatchNorm2d(16),
            torch.nn.ReLU()
        )
        self.conv2 = torch.nn.Sequential(
            torch.nn.Conv2d(16, 32, 3, 2, 1),
            torch.nn.BatchNorm2d(32),
            torch.nn.ReLU()
        )
        self.conv3 = torch.nn.Sequential(
            torch.nn.Conv2d(32, 64, 3, 2, 1),
            torch.nn.BatchNorm2d(64),
            torch.nn.ReLU()
        )
        self.conv4 = torch.nn.Sequential(
            torch.nn.Conv2d(64, 64, 2, 2, 0),
            torch.nn.BatchNorm2d(64),
            torch.nn.ReLU()
        )
        self.mlp1 = torch.nn.Linear(2 * 2 * 64, 100)
        self.mlp2 = torch.nn.Linear(100, 3)

    def forward(self, x):
        x = self.conv1(x)
        x = self.conv2(x)
        x = self.conv3(x)
        x = self.conv4(x)
        x = self.mlp1(x.view(x.size(0), -1))
        x = self.mlp2(x)
        return x
