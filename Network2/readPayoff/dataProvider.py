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
            '''数据为10位
            [0-2]:agent_name,仅在读取每个文件的第一个数组包含
            [3-5]:payoff,
            [6-8]:player权重,权重仅在读取每个文件的第一个数组包含
            [9]:proposal的label,0为接收，1为拒绝
            '''

        # 元素开始事件处理
        def startElement(self, tag, attributes):
            if tag == 'knowledge':
                player_name = attributes['role-name']
                self.players[player_name] = {}
                self.players[player_name]['id'] = int(attributes['party-num'])
                self.players[player_name]['resources'] = int(attributes['resource'])
                weight = int(attributes['resource'])
                player_id = self.players[player_name]['id']
                self.piece_data[player_id + 6] = weight  # 读取权重信息
                # self.piece_data[player_id + 6] = 1.0 * weight  # 读取权重信息

            if tag == 'player':
                player_name = attributes['role-name']
                agent_name = attributes['path']
                player_id = self.players[player_name]['id']
                self.piece_data[player_id] = agent_name  # 读取agentName

            if tag == 'proposal':
                player_name = attributes['role-name']
                value = int(attributes['reward'])
                player_id = self.players[player_name]['id']
                self.piece_data[player_id + 3] = 1.0 * value

            if tag == 'ally':
                player_name = attributes['role-name']
                value = int(attributes['reward'])
                player_id = self.players[player_name]['id']
                self.piece_data[player_id + 3] = 1.0 * value

            if tag == 'response':
                player_name = attributes['role-name']
                player_id = self.players[player_name]['id']
                player_agree = attributes['agree']
                if (player_agree == 'false'):
                    self.piece_data[9] = 1  # proposal的label

        # 元素结束事件处理

        def endElement(self, tag):
            if tag == 'round':
                add_piece(self.piece_data)
                self.piece_data = [0] * 10

        # 内容事件处理
        def characters(self, content):
            pass

    parser = xml.sax.make_parser()
    parser.setFeature(xml.sax.handler.feature_namespaces, 0)
    Handler = MovieHandler()
    parser.setContentHandler(Handler)
    parser.parse(file_name)

    return all_data.copy()
