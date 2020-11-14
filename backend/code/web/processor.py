from code import api
from flask_restplus import Resource
from code.web.provider import Provider


@api.route('/api/web/all_info')
class Client(Resource):

    def get(self):
        return Provider.get_all_info()
