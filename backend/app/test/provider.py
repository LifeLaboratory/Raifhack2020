from backend.app import api
from flask_restplus import Resource, Api


class Provider:
    @classmethod
    def ping(cls):
        return {'msg': 'PONG!'}

    @classmethod
    def test_data(cls, data):
        return {'test_data': data}