import collections

import torch
from torch.utils.data import DataLoader, Dataset
from data_provider import get_file_data
import os


class agentData(Dataset):
    def __init__(self, data_dir, k):
        self.data = []
        self.label = []
        self.len = 0

        files = os.listdir(data_dir)
        for file_name in files:
            if (file_name[-2:] != 'ml'):
                continue

            file_path = os.path.join(data_dir, file_name)

            data_list = get_file_data(file_path)

            itemdata = collections.deque()
            num = 0
            for i in range(0, len(data_list) - k - 1):

                piece_data = []
                piece_data.clear()
                while num < k:
                    itemdata.append(data_list[num][0])
                    itemdata.append(data_list[num][1])
                    itemdata.append(data_list[num][2])
                    num += 1

                itemdataCopy = itemdata.copy()
                while itemdataCopy.__len__() > 0:
                    piece_data.append(itemdataCopy.popleft())
                itemdata.popleft()  # 去掉前三个数据
                itemdata.popleft()
                itemdata.popleft()
                itemdata.append(data_list[num][0])  # 增加后三个数据
                itemdata.append(data_list[num][1])
                itemdata.append(data_list[num][2])

                # piece_data = [0] * k * 3
                # for j in range(0, k):
                #     ind = k - j
                #     if i - ind >= 0:
                #         piece_data[j * 3 + 0] = data_list[ind][0]
                #         piece_data[j * 3 + 1] = data_list[ind][1]
                #         piece_data[j * 3 + 2] = data_list[ind][2]
                #
                # ind = i + 1
                self.data.append(piece_data)
                self.label.append(data_list[num])
                self.len += 1
                num += 1

    def __len__(self):
        return self.len

    def __getitem__(self, index):
        data = torch.Tensor(self.data[index])

        label = self.label[index].copy()
        for i in range(0, 3):
            if label[i] != 0:
                label.append(1)
            else:
                label.append(0)

        label = torch.Tensor(label)
        return data, label
