五子连珠判断算法见如下链接：
https://blog.csdn.net/windchen1000/article/details/91130550

<?xml version="1.0" encoding="UTF-8"?>

<?import java.lang.*?>
<?import javafx.scene.effect.*?>
<?import javafx.scene.control.*?>
<?import javafx.scene.effect.Blend?>
<?import javafx.scene.layout.*?>

<AnchorPane fx:id="root_pane" maxHeight="-Infinity" maxWidth="-Infinity" minHeight="-Infinity" minWidth="-Infinity" prefHeight="555.0" prefWidth="523.0" xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1" fx:controller="com.cheney.controller.MainController">
    <children>
        <Separator layoutY="518.0" prefHeight="4.0" prefWidth="523.0">
            <effect>
                <Blend />
            </effect>
        </Separator>
        <Label layoutX="14.0" layoutY="528.0" text="时间：" />
        <Label fx:id="lb_time" layoutX="57.0" layoutY="528.0" text="00:11" />
        <Label layoutX="406.0" layoutY="528.0" text="状态：" />
        <Label fx:id="lb_status" layoutX="459.0" layoutY="528.0" text="已连接" />
        <MenuBar layoutY="2.0" maxHeight="-Infinity" maxWidth="-Infinity" minHeight="-Infinity" minWidth="-Infinity" prefHeight="25.0" prefWidth="523.0">
            <menus>
                <Menu mnemonicParsing="false" text="设置">
                    <items>
                        <MenuItem fx:id="menu_setting" mnemonicParsing="false" onAction="#onSetting" text="基础设置" />
                        <MenuItem fx:id="menu_db_setting" mnemonicParsing="false" onAction="#onDbSetting" text="数据库设置" />
                        <MenuItem fx:id="menu_close" mnemonicParsing="false" onAction="#onClose" text="关闭" />
                    </items>
                </Menu>
                <Menu mnemonicParsing="false" text="帮助">
                    <items>
                        <MenuItem mnemonicParsing="false" text="关于" />
                    </items>
                </Menu>
            </menus>
        </MenuBar>
        <Label layoutX="127.0" layoutY="369.0" text="工单号" />
        <TextField fx:id="work_no" layoutX="183.0" layoutY="365.0" prefHeight="23.0" prefWidth="208.0" />
        <Button fx:id="btn_choice_project" layoutX="401.0" layoutY="365.0" mnemonicParsing="false" onAction="#onChoseProject" text="选择" />
        <TextField fx:id="tv_equipment_id" layoutX="183.0" layoutY="435.0" prefHeight="23.0" prefWidth="258.0" editable="false"/>
        <Label layoutX="91.0" layoutY="439.0" text="生产设备编码" />
        <Label layoutX="91.0" layoutY="473.0" text="生产设备名称" />
        <TextField fx:id="tv_equipment_name" layoutX="183.0" layoutY="469.0" prefHeight="23.0" prefWidth="258.0" />
      <TabPane fx:id="tab_panel" layoutY="25.0" maxHeight="323.0" maxWidth="523.0" minHeight="323.0" minWidth="523.0" prefHeight="323.0" prefWidth="523.0" tabClosingPolicy="UNAVAILABLE">
        <tabs>
          <Tab text="雷电冲击">
            <content>
              <AnchorPane minHeight="0.0" minWidth="0.0" prefHeight="188.0" prefWidth="468.0">
                     <children>
                          <Label layoutX="25.0" layoutY="35.0" text="实验数据路径" />
                          <TextField fx:id="tv_data_path" layoutX="109.0" layoutY="31.0" prefHeight="23.0" prefWidth="296.0" />
                          <Button fx:id="btn_choice_path" layoutX="420.0" layoutY="31.0" mnemonicParsing="false" onAction="#onChoice" text="选择" />
                          <Button fx:id="btn_start_ldcj" layoutX="109.0" layoutY="73.0" mnemonicParsing="false" onAction="#onStartLDCJ" text="开始采集" />
                          <Button fx:id="btn_stop_ldcj" layoutX="341.0" layoutY="73.0" mnemonicParsing="false" onAction="#onStopLDCJ" text="停止采集" />
                        <TableView fx:id="table_ldcj_data" layoutY="102.0" prefHeight="194.0" prefWidth="523.0">
                          <columns>
                            <TableColumn fx:id="col_ldcj_time" prefWidth="125.0" text="采集时间" />
                            <TableColumn fx:id="col_head_time" prefWidth="99.0" text="波头时间（us）" />
                              <TableColumn fx:id="col_tail_time" prefWidth="94.0" text="波尾时间（us）" />
                              <TableColumn fx:id="col_peak_value" prefWidth="102.0" text="峰值（kV）" />
                              <TableColumn fx:id="col_wave_path" prefWidth="102.0" text="波形" />
                          </columns>
                        </TableView>
                     </children>
                  </AnchorPane>
            </content>
          </Tab>
          <Tab text="工频实验">
            <content>
              <AnchorPane minHeight="0.0" minWidth="0.0" prefHeight="183.0" prefWidth="379.0">
                     <children>
                        <VBox layoutX="14.0" layoutY="14.0" prefHeight="127.0" prefWidth="170.0" spacing="10.0">
                           <children>
                              <HBox alignment="CENTER_LEFT" prefHeight="28.0" prefWidth="158.0">
                                 <children>
                                    <Label alignment="CENTER" minWidth="60.0" text="端口" />
                                    <ComboBox fx:id="cb_port" prefHeight="23.0" prefWidth="100.0" />
                                 </children>
                              </HBox>
                              <HBox alignment="CENTER_LEFT" prefHeight="28.0" prefWidth="158.0">
                                 <children>
                                    <Label alignment="CENTER" minWidth="60.0" text="波特率" />
                                    <ComboBox fx:id="cb_baud_rate" prefHeight="23.0" prefWidth="100.0" />
                                 </children>
                              </HBox>
                              <HBox alignment="CENTER_LEFT" prefHeight="28.0" prefWidth="158.0">
                                 <children>
                                    <Label alignment="CENTER" minWidth="60.0" text="模式" />
                                    <ComboBox fx:id="cb_prity_mode" prefHeight="23.0" prefWidth="100.0" />
                                 </children>
                              </HBox>
                              <HBox alignment="CENTER_LEFT" prefHeight="29.0" prefWidth="138.0">
                                 <children>
                                    <Label alignment="CENTER" minWidth="60.0" text="设备ID" />
                                    <TextField fx:id="tv_slave_id" prefHeight="23.0" prefWidth="100.0" />
                                 </children>
                              </HBox>
                           </children>
                        </VBox>
                        <Label fx:id="tv_peak_value" layoutX="278.0" layoutY="244.0" prefHeight="15.0" prefWidth="61.0" text="0.0" />
                        <Label layoutX="202.0" layoutY="244.0" text="当前峰值：" />
                        <VBox layoutX="14.0" layoutY="161.0" prefHeight="119.0" prefWidth="170.0" spacing="10.0">
                           <children>
                              <HBox alignment="CENTER_LEFT" prefHeight="28.0" prefWidth="155.0">
                                 <children>
                                    <HBox alignment="CENTER_LEFT" prefHeight="28.0" prefWidth="182.0">
                                       <children>
                                          <Label alignment="CENTER" minWidth="60.0" text="峰值类型" />
                                          <ComboBox fx:id="cb_peak_type" prefHeight="23.0" prefWidth="100.0" />
                                       </children>
                                    </HBox>
                                 </children>
                              </HBox>
                              <HBox alignment="CENTER_LEFT" prefHeight="28.0" prefWidth="170.0">
                                 <children>
                                    <Label alignment="CENTER" minWidth="60.0" text="电压单位" />
                                    <ComboBox fx:id="cb_unit_type" prefHeight="23.0" prefWidth="100.0" />
                                 </children>
                              </HBox>
                              <HBox alignment="CENTER_LEFT" prefHeight="28.0" prefWidth="170.0" spacing="30.0">
                                 <children>
                                    <Button fx:id="btn_start_gp" mnemonicParsing="false" onAction="#opStartGP" prefHeight="5.0" prefWidth="64.0" text="开始采集" />
                                    <Button fx:id="btn_stop_gp" mnemonicParsing="false" onAction="#onStopGP" text="停止采集" />
                                 </children>
                              </HBox>
                           </children>
                        </VBox>
                        <TableView fx:id="table_gp_data" layoutX="195.0" prefHeight="239.0" prefWidth="328.0">
                          <columns>
                            <TableColumn fx:id="col_gp_time" prefWidth="177.0" text="采集时间" />
                            <TableColumn fx:id="col_gp_value" prefWidth="148.0" text="峰值电压" />
                          </columns>
                        </TableView>
                     </children>
                  </AnchorPane>
            </content>
          </Tab>
            <Tab text="局放实验">
              <content>
                <AnchorPane minHeight="0.0" minWidth="0.0" prefHeight="288.0" prefWidth="523.0">
                     <children>
                        <VBox layoutX="-10.0" layoutY="-9.0" prefHeight="200.0" prefWidth="100.0" />
                        <Button fx:id="btn_start_jf" layoutX="229.0" layoutY="14.0" mnemonicParsing="false" onAction="#onStartJfCollect" text="开始采集" />
                        <Button fx:id="btn_stop_jf" layoutX="342.0" layoutY="14.0" mnemonicParsing="false" onAction="#onStopJfCollect" text="停止采集" />
                        <TableView fx:id="table_jf_data" layoutY="48.0" prefHeight="249.0" prefWidth="523.0">
                          <columns>
                            <TableColumn fx:id="col_time" prefWidth="191.0" text="采集时间" />
                            <TableColumn fx:id="col_pc" minWidth="9.0" prefWidth="187.0" text="放电量（pC）" />
                          </columns>
                        </TableView>
                        <ComboBox fx:id="cb_pc_type" layoutX="77.0" layoutY="14.0" prefHeight="23.0" prefWidth="94.0" />
                     </children>
                  </AnchorPane>
              </content>
            </Tab>
        </tabs>
      </TabPane>
      <Separator layoutY="346.0" prefHeight="4.0" prefWidth="523.0" />
      <Button fx:id="btn_choice_standard" layoutX="401.0" layoutY="400.0" mnemonicParsing="false" onAction="#onChoseStandard" text="选择" />
      <TextField fx:id="tv_standard" layoutX="183.0" layoutY="400.0" prefHeight="23.0" prefWidth="208.0" editable="false" />
      <Label layoutX="115.0" layoutY="404.0" text="实验标准" />

    </children>
</AnchorPane>
