SQL_SELECT_INFO_CLIENT = """
SELECT *
FROM raifhack.clients
WHERE id={id}
"""

SQL_SELECT_ORDERS_COURIER = """
SELECT *
FROM raifhack.orders
WHERE number_courier={id}
"""
