import torch 
import torch.nn as nn

class LinearNet(nn.Module):
    def __init__(self):
        super(LinearNet, self).__init__()
        

        fc_list = [18, 32, 64, 32, 16]

        seq_list = []
        for i in range(len(fc_list) - 1):
            seq_list.append(nn.Linear(fc_list[i], fc_list[i + 1]))
            seq_list.append(nn.ReLU())
        self.feature = nn.Sequential(*seq_list)


        fc_list = [16, 16, 16, 3]
        seq_list = []
        for i in range(len(fc_list) - 1):
            seq_list.append(nn.Linear(fc_list[i], fc_list[i + 1]))
            seq_list.append(nn.ReLU())
        self.union_predict = nn.Sequential(*seq_list)

        fc_list = [16, 16, 8, 3]
        seq_list = []
        for i in range(len(fc_list) - 1):
            seq_list.append(nn.Linear(fc_list[i], fc_list[i + 1]))
            seq_list.append(nn.ReLU())
        self.score_predict = nn.Sequential(*seq_list)
    
    
    def forward(self, x):
        feature_map = self.feature(x)
        score = self.score_predict(feature_map)
        union = self.union_predict(feature_map)
        output = torch.cat((score, union), dim=1)
        return output