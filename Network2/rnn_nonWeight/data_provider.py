import xml.sax


def get_file_data(file_name):
    all_data = []

    def add_piece(piece_data):
        all_data.append(piece_data)

    class MovieHandler(xml.sax.ContentHandler):
        def __init__(self):
            self.players = {}
            self.players_number = 0
            self.piece_data = [0] * 10
            # self.piece_data = [0] * 7
            '''数据为10位
            [0-2]:提议者id置1
            [3-5]:payoff,
            [6-8]:player是否接收提议，state0：提议不包含，state1：接受，state2：拒绝
            [9]:proposal的label,0为接收，1为拒绝
            '''

        # 元素开始事件处理
        def startElement(self, tag, attributes):
            if tag == 'knowledge':
                player_name = attributes['role-name']
                self.players[player_name] = {}
                self.players[player_name]['id'] = int(attributes['party-num'])
                self.players[player_name]['resources'] = int(attributes['resource'])

            if tag == 'proposal':
                player_name = attributes['role-name']
                value = int(attributes['reward'])
                player_id = self.players[player_name]['id']
                self.piece_data[player_id] = 1  # 提议者置1
                self.piece_data[player_id + 3] = 1.0 * value / 100
                self.piece_data[player_id + 6] = 1  # 提议者视为接收联盟，state置为1

            if tag == 'ally':
                player_name = attributes['role-name']
                value = int(attributes['reward'])
                player_id = self.players[player_name]['id']
                self.piece_data[player_id + 3] = 1.0 * value / 100

            if tag == 'response':
                player_name = attributes['role-name']
                player_id = self.players[player_name]['id']
                player_agree = attributes['agree']
                if (player_agree == 'true'):
                    self.piece_data[player_id + 6] = 1  # player接受联盟，state置为1
                if (player_agree == 'false'):
                    self.piece_data[player_id + 6] = 2  # player拒绝联盟，state置为2
                    self.piece_data[9] = 1  # proposal的label

        # 元素结束事件处理
        def endElement(self, tag):
            if tag == 'round':
                add_piece(self.piece_data)
                self.piece_data = [0] * 10
                # self.piece_data = [0] * 7

        # 内容事件处理
        def characters(self, content):
            pass

    parser = xml.sax.make_parser()
    parser.setFeature(xml.sax.handler.feature_namespaces, 0)
    Handler = MovieHandler()
    parser.setContentHandler(Handler)
    parser.parse(file_name)

    return all_data.copy()


'''
if ( __name__ == "__main__"):

   # 创建一个 XMLReader
   parser = xml.sax.make_parser()
   # turn off namepsaces
   parser.setFeature(xml.sax.handler.feature_namespaces, 0)

   # 重写 ContextHandler
   Handler = MovieHandler()
   parser.setContentHandler(Handler)

   parser.parse("./data/150835480_Scene0.xml")
   print(all_data)

'''
