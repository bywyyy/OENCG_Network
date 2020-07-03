import torch.nn.functional as F
import torch.nn as nn
import time

timen = time.strftime("%m%d%H%M%S")
dataPath = '../saveData/mlp_nw' + timen + '.xls'

num = 0


def setnum(int):
    global num
    num = int


class LinearNet(nn.Module):
    def __init__(self):
        super(LinearNet, self).__init__()

        inputNum = num * 9 - 3
        fc_list = [inputNum, inputNum + 12, inputNum]

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

        fc_list = [inputNum, 8, 2]
        seq_list = []
        for i in range(len(fc_list) - 1):
            seq_list.append(nn.Linear(fc_list[i], fc_list[i + 1]))
            seq_list.append(nn.Dropout(0.4))
            seq_list.append(nn.LeakyReLU())
        self.prob_predict = nn.Sequential(*seq_list)

    def forward(self, x):
        feature_map = self.feature(x)
        score = self.prob_predict(feature_map)
        # union = self.union_predict(feature_map)
        # output = torch.cat((score, union), dim=1)
        # output = torch.cat((score), dim=1)
        output = F.softmax(score, dim=1)
        return output
