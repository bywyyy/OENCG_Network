from agent_Model import CNNnet
from agentDataSet import agentData
from MLP_utils import _test, _train

from torch.utils.data import DataLoader

if __name__ == '__main__':
    # linear_net = LinearNet([18, 32, 64, 32, 3])
    cnn_net = CNNnet()
    
    train_data = agentData('../data', 6)
    test_data = agentData('../test', 6)

    train_dl = DataLoader(train_data, 2, shuffle=False)
    test_dl = DataLoader(test_data, 1, shuffle=True)
    
    
    # 50 epoch learning_rate = 0.0001
    
    for i in _train(cnn_net, train_dl, 200, 0.001):
        _test(cnn_net, test_data)