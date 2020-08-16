import torch
from torch.utils.data import DataLoader, Dataset

from data_provider import get_file_data
import os


class agentData(Dataset):
    def __init__(self, data_dir):
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
                provideiData = [0] * 3
                offerData = [0] * 3
                weightData = [0] * 3

                for p in range(0, 3):
                    provideiData[p] = data_list[i][p]
                    offerData[p] = data_list[i][p + 3]
                    weightData[p] = data_list[0][p + 9]

                self.data.append([provideiData, offerData,weightData])
                self.label.append(data_list[i][12])
                self.len += 1

    def __len__(self):
        return self.len

    def __getitem__(self, index):
        dataitem = self.data[index]
        labelitem = self.label[index]
        data = torch.Tensor(dataitem)
        label = torch.Tensor([labelitem])
        return data, label
