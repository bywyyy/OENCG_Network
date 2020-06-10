from load_Model import loadModel

se = [1, 3, 6, 10, 15, 20]

if __name__ == '__main__':

    cnt = 0
    for k in se:
        loadModel(se[cnt])
        cnt += 1
