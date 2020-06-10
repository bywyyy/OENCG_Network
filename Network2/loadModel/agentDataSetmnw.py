import collections
import torch
from torch.utils.data import Dataset
from data_provider import get_file_data
import os


class agentDatamnw(Dataset):
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

            providerData = collections.deque()
            # payoffData = collections.deque()
            # stateData = collections.deque()
            num = 0
            for i in range(0, len(data_list) - k):
                piece_provider = []
                piece_payoff = []
                piece_state = []
                piece_provider.clear()
                piece_payoff.clear()
                piece_state.clear()
                while num < k:
                    providerData.append(data_list[num][0])
                    providerData.append(data_list[num][1])
                    providerData.append(data_list[num][2])
                    providerData.append(data_list[num][3])
                    providerData.append(data_list[num][4])
                    providerData.append(data_list[num][5])
                    num += 1

                providerCopy = providerData.copy()
                while providerCopy.__len__() > 0:
                    piece_provider.append(providerCopy.popleft())
                for p in range(6):
                    providerData.popleft()
                    providerData.append(data_list[num][p])
                self.data.append(piece_provider)
                self.label.append(data_list[num - 1][-1])
                self.len += 1
                num += 1

    def __len__(self):
        return self.len

    def __getitem__(self, index):
        dataitem = self.data[index]
        labelitem = self.label[index]
        data = torch.Tensor(dataitem)
        label = torch.Tensor([labelitem])
        return data, label

    # def __getitem__(self, index):
    #     data = torch.Tensor(self.data[index])
    #
    #     label = self.label[index].copy()
    #     for i in range(0, 3):
    #         if label[i] != 0:
    #             label.append(1)
    #         else:
    #             label.append(0)
    #
    #     label = torch.Tensor(label)
    #     return data, label
