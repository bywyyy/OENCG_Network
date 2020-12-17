import numpy as np
import torch
from torch.autograd import Variable

def _test(model, dst):

    model.eval()
    accuracy = 0.0
    accuracyRate = 0.0
    for i in range(0, dst.__len__()):
        data, label = dst[i]
        data = torch.Tensor([data.numpy()])
        data = Variable(data.float(), requires_grad=False)
        # data = Variable(torch.unsqueeze(data, dim=1).float(), requires_grad=False)
        with torch.no_grad():
            outputs = model(data)
        outputs2 = outputs[0].detach().numpy()
        np.set_printoptions(precision=6, suppress=True)

        payoff = data.numpy()
        payoff2 = payoff[0][1]
        payoff3 = list(map(int, payoff2[-3:] * 100))

        if outputs2[int(label[0])] > 0.5:
            accuracy += 1

        accuracyRate = accuracy / (i + 1)
    print("payoff: {}, ground truth: {},  outputs : {}, accuracy : {:.4f}".format(payoff3, label, outputs2,
                                                                                  accuracyRate))
    return accuracyRate
