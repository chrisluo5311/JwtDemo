package com.example.logindemo.tgbot.request;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 回传架构
 * {
 *   "updateId": "999990000",
 *   "Message": {
 *     "messageId": "999",
 *     "date": "1579875125",
 *     "text": "hi",
 *     "from": {
 *       "id": "913456635",
 *       "isBot": false,
 *       "firstName": "firstName",
 *       "lastName": "lastName",
 *       "userName": "userName",
 *       "languageCode": "zh@collation=stroke"
 *     },
 *     "chat": {
 *       "id": "913456635",
 *       "firstName": "firstName",
 *       "lastName": "lastName",
 *       "userName": "userName",
 *       "type": "private"
 *     }
 *   }
 * }
 * @author chris
 */
@ApiModel(value="tg message")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelegramMessage {

    private Long updateId;

    private Object Message;

}
