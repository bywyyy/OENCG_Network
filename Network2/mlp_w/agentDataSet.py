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
            if (file_name[-2:] == 're'):
                continue

            file_path = os.path.join(data_dir, file_name)

            data_list = get_file_data(file_path)
            piece_weight = []
            piece_weight.clear()
            piece_weight.append(data_list[0][9])
            piece_weight.append(data_list[0][10])
            piece_weight.append(data_list[0][11])

            providerData = collections.deque()
            # payoffData = collections.deque()
            num = 0
            for i in range(0, len(data_list) - k):
                piece_provider = []
                piece_payoff = []
                piece_provider.clear()
                piece_payoff.clear()
                while num < k:
                    providerData.append(data_list[num][0])
                    providerData.append(data_list[num][1])
                    providerData.append(data_list[num][2])
                    providerData.append(data_list[num][3])
                    providerData.append(data_list[num][4])
                    providerData.append(data_list[num][5])
                    providerData.append(data_list[num][6])
                    providerData.append(data_list[num][7])
                    providerData.append(data_list[num][8])
                    # providerData.append(data_list[num][0])
                    # providerData.append(data_list[num][1])
                    # providerData.append(data_list[num][2])
                    # payoffData.append(data_list[num][3])
                    # payoffData.append(data_list[num][4])
                    # payoffData.append(data_list[num][5])
                    # piece_weight.append(data_list[0][6])
                    # piece_weight.append(data_list[0][7])
                    # piece_weight.append(data_list[0][8])
                    num += 1

                providerCopy = providerData.copy()
                # payoffCopy = payoffData.copy()
                while providerCopy.__len__() > 0:
                    piece_provider.append(providerCopy.popleft())
                    # piece_payoff.append(payoffCopy.popleft())

                # 去掉前三个数据，增加后三个数据，对数据进行更新
                for p in range(9):
                    providerData.popleft()
                    providerData.append(data_list[num][p])
                    # payoffData.popleft()
                    # payoffData.append(data_list[num][p + 3])

                piece_provider = piece_provider[0:-3]
                # piece_provider.extend(piece_payoff)
                piece_provider.extend(piece_weight)
                self.data.append(piece_provider)
                self.label.append(data_list[num - 1][12])
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
