from agent_Modelcnnw import CNNnet
from agent_Modelcnnw import setnum
from agentDataSet import agentData
from MLP_utils import _test, _train
from save_Model import saveModel
from torch.utils.data import DataLoader

# se = [1, 3, 6, 10, 15, 20]
# point = [64, 128, 320, 512, 704, 960]
# se = [1, 2, 4, 7, 11, 16, 21]
# point = [64, 128, 192, 320, 512, 768, 1024]
se = [21]
point = [1024]

global globalk
globalk = se[0]

if __name__ == '__main__':
    cnt = 0
    for k in se:
        globalk = k

        setnum(point[cnt])
        cnt += 1
        cnn_net = CNNnet()

        train_data = agentData('../alldata/data', k)
        test_data = agentData('../alldata/test', k)

        train_dl = DataLoader(train_data, 4, shuffle=False)
        test_dl = DataLoader(test_data, 1, shuffle=False)

        jishu = 0
        accuracyRateMax = 0.0
        for i in _train(cnn_net, train_dl, 80, 0.001):
            jishu += 1
            if jishu % 1 == 0:
                accuracyRate = _test(cnn_net, test_data)
                if accuracyRate > accuracyRateMax:
                    accuracyRateMax = accuracyRate
                    saveModel(cnn_net)

        print('=============================================================================')
