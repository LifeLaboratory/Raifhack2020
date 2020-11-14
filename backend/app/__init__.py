from flask import Flask
from flask_restplus import Api
from flask_cors import CORS

app = Flask(__name__)
cors = CORS(app, resources={r"/api/*": {"origins": "*"}})
api = Api(app=app)


import backend.app.test.processor
