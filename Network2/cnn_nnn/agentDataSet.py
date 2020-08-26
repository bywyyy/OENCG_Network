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

            # providerData = collections.deque()
            payoffData = collections.deque()
            # stateData = collections.deque()
            num = 0
            for i in range(0, len(data_list)):
                # piece_provider = []
                piece_payoff = []
                # piece_state = []
                # piece_provider.clear()
                piece_payoff.clear()
                # piece_state.clear()

                while num < k:
                    payoffData.append(0)
                    payoffData.append(0)
                    payoffData.append(0)
                    num += 1

                # 去掉前三个数据，增加后三个数据，对数据进行更新
                for p in range(3):
                    payoffData.popleft()
                    payoffData.append(data_list[i][p + 3])

                # providerCopy = providerData.copy()
                payoffCopy = payoffData.copy()
                # stateCopy = stateData.copy()
                while payoffCopy.__len__() > 0:
                    # piece_provider.append(providerCopy.popleft())
                    piece_payoff.append(payoffCopy.popleft())
                    # piece_state.append(stateCopy.popleft())

                self.data.append([piece_payoff])
                self.label.append(data_list[i][9])
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
