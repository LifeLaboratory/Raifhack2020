from flask import Flask
from flask_restplus import Api
from flask_cors import CORS

app = Flask(__name__, static_folder='gen_qrcode')
app.config["JSON_AS_ASCII"] = False
cors = CORS(app, resources={r"/api/*": {"origins": "*"}})
api = Api(app=app)


import code.test.processor
import code.order.processor
import code.courier.processor
import code.client.processor
import code.web.processor