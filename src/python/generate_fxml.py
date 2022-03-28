
with open("src/python/pane_tags.txt", "w") as file:
    for row in range(0, 8):
        for col in range(0, 8):
            file.write('<Pane fx:id="tile{}{}" onMouseClicked="#testClick" prefHeight="100.0" prefWidth="100.0" GridPane.columnIndex="{}" GridPane.rowIndex="{}" id="{}{}" />\n'.format(row, col, col, 7-row, row, col))


with open("src/python/controller_tile_ids.txt", "w") as file:
    for row in range(0, 8):
        for col in range(0, 8):
            file.write("sprite{}{}, ".format(row, col))


with open("src/python/sprite_tags.txt", "w") as file:
    for row in range(0, 8):
        for col in range(0, 8):
            file.write('<ImageView fx:id="sprite{}{}" id="{}{}" fitHeight="100.0" fitWidth="100.0" pickOnBounds="true" preserveRatio="true" GridPane.columnIndex="{}" GridPane.rowIndex="{}">\n<GridPane.margin>\n<Insets left="17.5" />\n</GridPane.margin>\n</ImageView>\n'.format(row, col, row, col, col, 7-row))
            