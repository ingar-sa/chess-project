""" 
with open("fxml_tags.txt", "w") as file:
    for row in range(0, 8):
        for col in range(0, 8):
            file.write('<Pane fx:id="tile{}{}" prefHeight="98.0" prefWidth="98.0" GridPane.columnIndex="{}" GridPane.rowIndex="{}" />\n'.format(row, col, col, 7-row))


with open("controller_tile_ids.txt", "w") as file:
    for row in range(0, 8):
        for col in range(0, 8):
            file.write("sprite{}{}, ".format(row, col))
"""

with open("fxml_tags.txt", "w") as file:
    for row in range(0, 8):
        for col in range(0, 8):
            file.write('<ImageView fx:id="sprite{}{}" fitHeight="65.0" fitWidth="65.0" pickOnBounds="true" preserveRatio="true" GridPane.columnIndex="{}" GridPane.rowIndex="{}">\n<GridPane.margin>\n<Insets left="17.5" />\n</GridPane.margin>\n</ImageView>\n'.format(row, col, col, 7-row))