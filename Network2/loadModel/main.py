import sys

from load_Model import loadModel

#
# se = [1, 3, 6, 10, 15, 20]

if __name__ == '__main__':
    # cnt = 0
    # for k in se:
    #     loadModel(se[cnt])
    #     cnt += 1
    # strs = ''

    a = []
    for i in range(1, len(sys.argv)):
        a.append((int(sys.argv[i])))

    acc = loadModel(int(a[0]), int(a[1]),int(a[2]))
    '''
    参数1：K值
    参数2：playerNum
    参数3：网络类型。1:MLP无权重,2:MLP有权重,3:CNN无权重,4:CNN有权重
    '''
    # acc = loadModel(2, 0, 1)
    # print('========================' + str(acc) + '==============================')
