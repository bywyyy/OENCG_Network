import sys
from load_Model import loadModel

# print(sys.path)

se = [1, 3, 6, 10, 15, 20]


if __name__ == '__main__':
    # cnt = 0
    # for k in se:
    #     loadModel(se[cnt])
    #     cnt += 1
    # strs = ''

    a = []
    for i in range(1, len(sys.argv)):
        a.append((int(sys.argv[i])))

    acc = loadModel(int(a[0]),int(a[1]))
    # acc = loadModel(6, 2)
    # print('========================' + str(acc) + '==============================')
