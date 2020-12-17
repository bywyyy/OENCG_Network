from agent_Modelcnnw import CNNnet
from agent_Modelcnnw import setnum
from agentDataSet import agentData
from MLP_utils import _test, _train
from save_Model import saveModel
from torch.utils.data import DataLoader

# se = [1, 2, 4, 7, 11, 16, 21]
# point = [64, 128, 192, 320, 512, 768, 1024]

# se = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21]
# point = [64, 128, 128, 192, 256, 320, 320, 384, 448, 512, 512, 576, 640, 704, 704, 768, 832, 896, 896, 960, 1024]

se = [21]
point = [1024]


# se = [21]
# point = [1024]

if __name__ == '__main__':
    cnt = 0
    for k in se:
        setnum(point[cnt])
        cnt += 1
        cnn_net = CNNnet(k)

        train_data = agentData('../alldata/data', k)
        test_data = agentData('../alldata/test', k)

        train_dl = DataLoader(train_data, 16, shuffle=True, drop_last=True)
        test_dl = DataLoader(test_data, 1, shuffle=False, drop_last=True)

        jishu = 0
        accuracyRateMax = 0.0
        for i in _train(cnn_net, train_dl, 15, 1e-4):
            jishu += 1
            if jishu % 1 == 0:
                accuracyRate = _test(cnn_net, test_data, k)
                if accuracyRate > accuracyRateMax:
                    accuracyRateMax = accuracyRate
                    saveModel(cnn_net)

        print('=============================================================================')
