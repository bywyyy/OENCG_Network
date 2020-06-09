from agent_Model import CNNnet
from agent_Model import setnum
from agent_Model import setk
from agentDataSet import agentData
from MLP_utils import _test, _train
from torch.utils.data import DataLoader

se = [1, 3, 6, 10, 15, 20]
point = [64, 256, 576, 960, 1408, 1920]

# se = [1]
# point = [64]
global globalk
globalk = se[0]

if __name__ == '__main__':

    cnt = 0
    for k in se:
        globalk = k

        setnum(point[cnt])
        setk(se[cnt])
        cnt += 1
        cnn_net = CNNnet()

        train_data = agentData('../allData/data', k)
        test_data = agentData('../allData/test', k)

        train_dl = DataLoader(train_data, 4, shuffle=False)
        test_dl = DataLoader(test_data, 1, shuffle=False)

        jishu = 0
        for i in _train(cnn_net, train_dl, 100, 0.0001):
            jishu += 1
            if jishu % 1 == 0:
                _test(cnn_net, test_data)

        print('=============================================================================')
        # for i in range(50):
        #     _train(cnn_net, train_dl, 1, 0.0001)
        #     _test(cnn_net, test_data)
        #     print('===============================================================================================')
