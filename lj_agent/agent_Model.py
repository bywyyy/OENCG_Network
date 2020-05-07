import torch
import torch.nn as nn

class LinearNet(nn.Module):
    def __init__(self, fc_list):
        super(LinearNet, self).__init__()
        
        seq_list = [nn.BatchNorm1d(fc_list[0])]
        
        for i in range(len(fc_list) - 1):
            seq_list.append(nn.Linear(fc_list[i], fc_list[i + 1]))
            seq_list.append(nn.Sigmoid())
        self.feature = nn.Sequential(*seq_list)
    
    
    def forward(self, x):
        x = self.feature(x)
        return x