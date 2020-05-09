import torch
import torch.nn as nn
import random
from torch.autograd import Variable


def _train(model, dl, num_epochs, learning_rate):
    # optimizer = torch.optim.Adam(model.parameters(), lr=learning_rate, weight_decay=0.01)

    optimizer = torch.optim.Adam(model.parameters(), lr=learning_rate)
    criterion = nn.MSELoss()

    for epoch in range(num_epochs):
        i = 0
        cur_cnt = 0
        total_cnt = 0
        for (item_data, item_label) in dl:
            batch_x = Variable(torch.unsqueeze(item_data, dim=1).float(), requires_grad=False)
            batch_y = Variable(torch.unsqueeze(item_label, dim=1).float(), requires_grad=False)

            outputs = model(batch_x)

            optimizer.zero_grad()

            loss = criterion(outputs, batch_y)
            loss.backward()
            optimizer.step()

            total_cnt += 1
            i += 1
        if (epoch + 1) % 1 == 0:
            print('Epoch [{}/{}], Setp [{}], LOSS: {:.4f}, Total Sample {}'
                  .format(epoch + 1, num_epochs, i, loss.item(), total_cnt))
            yield
            model.train()


def _test(model, dst):
    cur_cnt = 0
    total = 0
    model.eval()

    rand_idx = random.randint(0, dst.__len__())
    data, label = dst[rand_idx]

    data = torch.Tensor([data.numpy()])
    data = Variable(torch.unsqueeze(data, dim=1).float(), requires_grad=False)
    outputs = model(data)
    print(outputs)
    outputs = outputs[0]
    sum_data = outputs[0] + outputs[1] + outputs[2]
    outputs[0] = outputs[0] / sum_data * 100
    outputs[1] = outputs[1] / sum_data * 100
    outputs[2] = outputs[2] / sum_data * 100

    print("ground truth: {},  outputs : {}".format(label, outputs))
