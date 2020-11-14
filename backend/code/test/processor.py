from code import api
from flask_restplus import Resource, fields
from code.test.provider import Provider

a_test = api.model('Test', {'msg': fields.String('PONG!')})
a_test2 = api.model('Test2', {'data': fields.String('test_data')})


@api.route('/api/test')
class Test(Resource):

    def get(self, **kwargs):
        return Provider.ping()


@api.route('/api/test2')
class Test2(Resource):

    def get(self, **kwargs):
        return Provider.test_data(123)
