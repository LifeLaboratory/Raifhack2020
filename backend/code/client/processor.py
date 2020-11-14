from code import api
from flask_restplus import Resource, fields
from code.client.provider import Provider

a_test = api.model('Test', {'msg': fields.String('PONG!')})
a_test2 = api.model('Test2', {'data': fields.String('test_data')})


@api.route('/api/client/<client_id>')
class Test(Resource):

    def get(self, client_id):
        return Provider.get_client_info(client_id)
