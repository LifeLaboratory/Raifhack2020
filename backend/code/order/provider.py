from code import api
from flask_restplus import Resource, Api
from code.base.base_sql import Sql
from code.sql.sql_query import *
from code.utils import sbp_client


class Provider:
    @classmethod
    def create_order(cls, args):
        query = SQL_INSERT_ORDER_CREATE.format(**args)
        return Sql.exec(query=query)

    @classmethod
    def update_status_orders(cls):
        query = SQL_SELECT_ORDER_STATUS
        answer = Sql.exec(query=query)
        for ans in answer:
            client = sbp_client.SBPClient()
            secret = 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNQTQ3MzI3MSIsImp0aSI6IjIwOWI3MjNkLTdmZm' \
                     'ItNDZhZi05YzU4LWFmZjFiMWI4YzRlNSJ9.lLZtvflKgxHPTmaZbH4cnOw2_1NE_f4LFP7fbVVnOSc'
            client.add_secret_key(secret)
            client.add_merchant_id('MA473271')
            answ = client.get_transaction(ans["qr_id"])
            if answ.get("paymentStatus") == "SUCCESS":
                quer = SQL_UPDATE_OREDER_STATUS.format(pays=answ.get("paymentStatus"),
                                                       date=answ.get("transactionDate"),
                                                       id=ans["id"])
                Sql.exec(query=quer)
