import collections
import torch
from torch.utils.data import Dataset
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
                    # providerData.append(data_list[num][0])
                    # providerData.append(data_list[num][1])
                    # providerData.append(data_list[num][2])
                    # payoffData.append(data_list[num][3])
                    # payoffData.append(data_list[num][4])
                    # payoffData.append(data_list[num][5])
                    # stateData.append(data_list[num][6])
                    # stateData.append(data_list[num][7])
                    # stateData.append(data_list[num][8])
                    num += 1

                providerCopy = providerData.copy()
                # payoffCopy = payoffData.copy()
                # stateCopy = stateData.copy()
                while providerCopy.__len__() > 0:
                    piece_provider.append(providerCopy.popleft())
                    # piece_payoff.append(payoffCopy.popleft())
                    # piece_state.append(stateCopy.popleft())

                # 去掉前三个数据，增加后三个数据，对数据进行更新
                for p in range(6):
                    providerData.popleft()
                    providerData.append(data_list[num][p])
                    # payoffData.popleft()
                    # payoffData.append(data_list[num][p + 3])
                    # stateData.popleft()
                    # stateData.append(data_list[num][p + 6])
                # piece_provider.extend(piece_payoff)
                # self.data.append([piece_provider, piece_payoff, piece_state])
                self.data.append(piece_provider)
                self.label.append(data_list[num - 1][6])
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