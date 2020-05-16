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
            if (file_name[-2:] == 're'):
                continue

            file_path = os.path.join(data_dir, file_name)

            data_list = get_file_data(file_path)

            for i in range(0, len(data_list) - 4):

                piece_data = [0] * (k + 1) * 3
                for j in range(0, k):
                    ind = k - j
                    if i - ind >= 0:
                        piece_data[j * 3 + 0] = data_list[ind][0]
                        piece_data[j * 3 + 1] = data_list[ind][1]
                        piece_data[j * 3 + 2] = data_list[ind][2]

                piece_data[k * 3] = data_list[0][3]  # 权重添加到最后
                piece_data[k * 3 + 1] = data_list[0][4]
                piece_data[k * 3 + 2] = data_list[0][5]

                ind = i + 1
                # 处理label数据，label只有收益分配，去掉后三位权重
                label_data = [data_list[ind][0], data_list[ind][1], data_list[ind][2]]
                self.data.append(piece_data)
                self.label.append(label_data)
                self.len += 1

    def __len__(self):
        return self.len

    def __getitem__(self, index):
        data = torch.Tensor(self.data[index])

        label = self.label[index].copy()
        # for i in range(0, 3):
        #     if label[i] != 0:
        #         label.append(1)
        #     else:
        #         label.append(0)

        label = torch.Tensor(label)
        return data, label
