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

            for i in range(0, len(data_list) - 1):

                piece_data = [0] * k * 4
                # singleLabel = [0] * 100
                for j in range(0, k):
                    ind = k - j
                    if i - ind >= 0:
                        piece_data[j * 4 + 0] = data_list[ind][0]
                        piece_data[j * 4 + 1] = data_list[ind][1]
                        piece_data[j * 4 + 2] = data_list[ind][2]
                        piece_data[j * 4 + 3] = data_list[ind][3]

                ind = i + 1

                # singleLabel[int(data_list[ind][1])] = 1
                self.data.append(piece_data)
                self.label.append(data_list[ind])
                self.len += 1

    def __len__(self):
        return self.len

    def __getitem__(self, index):
        data = torch.Tensor(self.data[index])
        label = torch.Tensor(self.label[index])
        return data, label
