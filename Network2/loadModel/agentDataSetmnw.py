import collections
import torch
from torch.utils.data import Dataset
from data_provider import get_file_data
import os


class agentDatamnw(Dataset):
    def __init__(self, data_dir, k, playerNum):
        self.data = []
        self.label = []
        self.len = 0

        preProvider = [0] * 3  # 当前待预测offer的提议者
        preProvider[playerNum] = 1

        files = os.listdir(data_dir)  # 列出目录的下所有文件和文件夹保存到lists
        print(list)
        files.sort(key=lambda fn: os.path.getmtime(data_dir + "/" + fn))  # 按时间排序

        file_name = os.path.join(data_dir, files[-1])
        if (file_name[-2:] == 're'):
            file_name = os.path.join(data_dir, files[-2])
        data_list = get_file_data(file_name)
        datalistLen = len(data_list)

        piece_data = []

        for i in range(datalistLen - k + 1, datalistLen):
            piece_data.append(data_list[i][0])
            piece_data.append(data_list[i][1])
            piece_data.append(data_list[i][2])
            piece_data.append(data_list[i][3])
            piece_data.append(data_list[i][4])
            piece_data.append(data_list[i][5])

        piece_data.append(preProvider[0])
        piece_data.append(preProvider[1])
        piece_data.append(preProvider[2])

        for player1 in range(0, 100, 2):
            for player2 in range(0, 100 - player1 + 1, 2):
                player3 = 100 - player1 - player2
                if player3 == 100 or player2 == 100:
                    continue
                hbdata = piece_data[:]
                itemdata = []
                itemdata.append(player1 / 100)
                itemdata.append(player2 / 100)
                itemdata.append(player3 / 100)
                hbdata.extend(itemdata)
                self.data.append(hbdata)
                self.len += 1
                itemdata.clear()

    def __len__(self):
        return self.len

    def __getitem__(self, index):
        dataitem = self.data[index]
        data = torch.Tensor(dataitem)
        return data
