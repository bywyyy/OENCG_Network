import collections
import torch
from torch.utils.data import Dataset
from data_provider import get_file_data
import os


class agentDatacw(Dataset):
    def __init__(self, data_dir, k, playerNum):
        self.data = []
        self.label = []
        self.len = 0

        preProvider = [0] * 3  # 当前待预测offer的提议者
        preProvider[playerNum] = 1

        files = os.listdir(data_dir)  # 列出目录的下所有文件和文件夹保存到lists
        # print(list)
        files.sort(key=lambda fn: os.path.getmtime(data_dir + "/" + fn))  # 按时间排序

        file_name = os.path.join(data_dir, files[-1])
        if (file_name[-2:] == 're'):
            file_name = os.path.join(data_dir, files[-2])
        data_list = get_file_data(file_name)
        datalistLen = len(data_list)
        majority = data_list[0][13]

        piece_provider = []
        piece_payoff = []
        piece_state = []
        piece_weight = []

        for i in range(datalistLen - k + 1, datalistLen):
            piece_provider.append(data_list[i][0])
            piece_provider.append(data_list[i][1])
            piece_provider.append(data_list[i][2])
            piece_payoff.append(data_list[i][3])
            piece_payoff.append(data_list[i][4])
            piece_payoff.append(data_list[i][5])
            piece_state.append(data_list[i][6])
            piece_state.append(data_list[i][7])
            piece_state.append(data_list[i][8])
            piece_weight.append(data_list[0][9])
            piece_weight.append(data_list[0][10])
            piece_weight.append(data_list[0][11])

        piece_provider.append(preProvider[0])
        piece_provider.append(preProvider[1])
        piece_provider.append(preProvider[2])
        piece_state.append(0)
        piece_state.append(0)
        piece_state.append(0)
        piece_weight.append(data_list[0][9])
        piece_weight.append(data_list[0][10])
        piece_weight.append(data_list[0][11])

        for player1 in range(0, 100, 5):
            for player2 in range(0, 100 - player1 + 1, 5):
                player3 = 100 - player1 - player2
                if player3 == 100 or player2 == 100:
                    continue

                hbdata = piece_payoff[:]
                itemdata = []
                itemdata.append(player1 / 100)
                itemdata.append(player2 / 100)
                itemdata.append(player3 / 100)
                hbdata.extend(itemdata)

                majoSum = 0
                for index in range(0, 3):
                    if (itemdata[index] > 0):
                        majoSum += data_list[0][9 + index]
                if majoSum >= majority:
                    self.data.append([piece_provider, hbdata, piece_state, piece_weight])
                    self.len += 1
                    itemdata.clear()
                else:
                    continue

    def __len__(self):
        return self.len

    def __getitem__(self, index):
        dataitem = self.data[index]
        data = torch.Tensor(dataitem)
        return data
