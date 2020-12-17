from agent_Model import CNNnet
from agent_Model import setnum
from agentDataSet import agentData
from MLP_utils import _test, _train
from torch.utils.data import DataLoader
from save_Model import saveModel

se = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21]
# point = [64, 128, 192, 384, 576, 768, 1024]

point = [64, 128, 192, 192, 256, 320, 384, 384, 448, 512, 576, 576, 640, 704, 768, 768, 832, 896, 960, 960, 1024]

# se = [21]
# point = [1024]

if __name__ == '__main__':

    cnt = 0
    for k in se:

        setnum(point[cnt])
        cnn_net = CNNnet(k)

        train_data = agentData('../allData/data', k)
        test_data = agentData('../allData/test', k)

        train_dl = DataLoader(train_data, 4, shuffle=False)
        test_dl = DataLoader(test_data, 1, shuffle=False)

        jishu = 0
        accuracyRateMax = 0.0
        for i in _train(cnn_net, train_dl, 20, 1e-6):
            jishu += 1
            if jishu % 1 == 0:
                accuracyRate = _test(cnn_net, test_data, k)
                if accuracyRate > accuracyRateMax:
                    accuracyRateMax = accuracyRate
                    saveModel(cnn_net)
        print('=============================================================================')

        cnt += 1
