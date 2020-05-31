from agent_Model import RNNnet
from agentDataSet import agentData
from MLP_utils import _test, _train

from torch.utils.data import DataLoader

if __name__ == '__main__':
    # linear_net = LinearNet([18, 32, 64, 32, 3])
    rnn_net = RNNnet(4, 256, 101, 2)

    train_data = agentData('../dataTest', 1)
    test_data = agentData('../test', 1)

    train_dl = DataLoader(train_data, 3, shuffle=False)
    test_dl = DataLoader(test_data, 1, shuffle=True)

    # 50 epoch learning_rate = 0.0001

    for i in _train(rnn_net, train_dl, 500, 0.001):
        _test(rnn_net, test_data)
