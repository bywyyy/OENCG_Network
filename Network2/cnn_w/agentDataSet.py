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

            providerData = collections.deque()
            payoffData = collections.deque()
            # stateData = collections.deque()
            num = 0
            for i in range(0, len(data_list) - k):
                piece_provider = []
                piece_payoff = []
                # piece_state = []
                piece_provider.clear()
                piece_payoff.clear()
                # piece_state.clear()
                while num < k:
                    providerData.append(data_list[num][0])
                    providerData.append(data_list[num][1])
                    providerData.append(data_list[num][2])
                    payoffData.append(data_list[num][3])
                    payoffData.append(data_list[num][4])
                    payoffData.append(data_list[num][5])
                    piece_weight.append(data_list[0][6])
                    piece_weight.append(data_list[0][7])
                    piece_weight.append(data_list[0][8])
                    # stateData.append(data_list[num][6])
                    # stateData.append(data_list[num][7])
                    # stateData.append(data_list[num][8])
                    # piece_weight.append(data_list[0][9])
                    # piece_weight.append(data_list[0][10])
                    # piece_weight.append(data_list[0][11])

                    num += 1

                providerCopy = providerData.copy()
                payoffCopy = payoffData.copy()
                # stateCopy = stateData.copy()
                while providerCopy.__len__() > 0:
                    piece_provider.append(providerCopy.popleft())
                    piece_payoff.append(payoffCopy.popleft())
                    # piece_state.append(stateCopy.popleft())

                # 去掉前三个数据，增加后三个数据，对数据进行更新
                for p in range(3):
                    providerData.popleft()
                    providerData.append(data_list[num][p])
                    payoffData.popleft()
                    payoffData.append(data_list[num][p + 3])
                    # stateData.popleft()
                    # stateData.append(data_list[num][p + 6])

                # self.data.append([piece_provider, piece_payoff, piece_state, piece_weight])
                # self.label.append(data_list[num - 1][12])
                self.data.append([piece_provider, piece_payoff, piece_weight])
                self.label.append(data_list[num - 1][9])
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
