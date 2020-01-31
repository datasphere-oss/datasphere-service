import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div>
    +             {hook.type == 'hipchat-project' ? (
    +                 <div>
    +                     <div className=\"control-group\">
    +                         <label className=\"control-label\">使用全局通道</label>
    +                         <div className=\"controls\">
    +                             <label>
    +                                 <input type=\"checkbox\" value={hook.configuration.useGlobalChannel} />
    +                                 <span className=\"help-inline\">使用一个管理员定义的通道</span>
    +                             </label>
    +                         </div>
    +                     </div>
    + 
    +                     {hook.configuration.useGlobalChannel ? (
    +                         <div
    +                             integration-channel-selector=\"\"
    +                             integration-type=\"'hipchat-project'\"
    +                             model=\"hook.configuration\"
    +                             field=\"channelId\">
    +                             {!hook.configuration.useGlobalChannel ? (
    +                                 <div className=\"control-group\">
    +                                     <label className=\"control-label\">Auth token</label>
    +                                     <div className=\"controls\">
    +                                         <input type=\"text\" value={hook.configuration.authorizationToken} />
    +                                         <span className=\"help-inline\">Hipchat API Auth token</span>
    +                                     </div>
    +                                 </div>
    +                             ) : null}
    +                             {!hook.configuration.useGlobalChannel ? (
    +                                 <div className=\"control-group\">
    +                                     <label className=\"control-label\">使用代理</label>
    +                                     <div className=\"controls\">
    +                                         <input type=\"checkbox\" value={hook.configuration.useProxy} />
    +                                         <span className=\"help-inline\">使用 DSS 代理设置连接 </span>
    +                                     </div>
    +                                 </div>
    +                             ) : null}
    + 
    +                             <div className=\"control-group\">
    +                                 <label className=\"control-label\">空间</label>
    +                                 <div className=\"controls\">
    +                                     <input type=\"text\" value={hook.configuration.room} />
    +                                     <span className=\"help-inline\">Name/Id of a Hipchat room</span>
    +                                 </div>
    +                             </div>
    +                             <div className=\"control-group\">
    +                                 <label className=\"control-label\">发送者</label>
    +                                 <div className=\"controls\">
    +                                     <input type=\"text\" value={hook.configuration.sender} />
    +                                     <span className=\"help-inline\">发送者名称</span>
    +                                 </div>
    +                             </div>
    +                             <div include-no-scope=\"/templates/projects/integrations/messaging-like-selection.html\" />
    + 
    +                             {hook.type == 'github' ? (
    +                                 <div>
    +                                     <div className=\"control-group\">
    +                                         <label className=\"control-label\">用户名</label>
    +                                         <div className=\"controls\">
    +                                             <input type=\"text\" value={hook.configuration.username} />
    +                                             <span className=\"help-inline\">User of the auth token</span>
    +                                         </div>
    +                                     </div>
    +                                     <div className=\"control-group\">
    +                                         <label className=\"control-label\">Auth token</label>
    +                                         <div className=\"controls\">
    +                                             <input type=\"text\" value={hook.configuration.accessToken} />
    +                                             <span className=\"help-inline\">个人访问 token</span>
    +                                         </div>
    +                                     </div>
    +                                     <div className=\"control-group\">
    +                                         <label className=\"control-label\">仓库</label>
    +                                         <div className=\"controls\">
    +                                             <input type=\"text\" value={hook.configuration.repository} />
    +                                             <span className=\"help-inline\">完成, 如: apache/hive</span>
    +                                         </div>
    +                                     </div>
    +                                 </div>
    +                             ) : null}
    + 
    +                             {hook.type == 'slack-project' ? (
    +                                 <div>
    +                                     <div className=\"control-group\">
    +                                         <label className=\"control-label\">使用全局通道</label>
    +                                         <div className=\"controls\">
    +                                             <label>
    +                                                 <input type=\"checkbox\" value={hook.configuration.useGlobalChannel} />
    +                                                 <span className=\"help-inline\">使用管理员定义的通道</span>
    +                                             </label>
    +                                         </div>
    +                                     </div>
    + 
    +                                     {hook.configuration.useGlobalChannel ? (
    +                                         <div
    +                                             integration-channel-selector=\"\"
    +                                             integration-type=\"'slack-project'\"
    +                                             model=\"hook.configuration\"
    +                                             field=\"channelId\">
    +                                             {!hook.configuration.useGlobalChannel ? (
    +                                                 <div className=\"control-group\">
    +                                                     <label className=\"control-label\">模式</label>
    +                                                     {hook.configuration ? (
    +                                                         <div
    +                                                             className=\"controls\"
    +                                                             ng-init=\"hook.configuration.mode = hook.configuration.mode || 'WEBHOOK';\">
    +                                                             <select value={hook.configuration.mode}>
    +                                                                 <option value=\"WEBHOOK\">使用流入的 webhook</option>
    +                                                                 <option value=\"API\">使用 API</option>
    +                                                             </select>
    +                                                         </div>
    +                                                     ) : null}
    +                                                 </div>
    +                                             ) : null}
    +                                             {hook.configuration.mode == 'API' &&
    +                                             !hook.configuration.useGlobalChannel ? (
    +                                                 <div className=\"control-group\">
    +                                                     <label className=\"control-label\">Authorization token</label>
    +                                                     <div className=\"controls\">
    +                                                         <input
    +                                                             type=\"text\"
    +                                                             value={hook.configuration.authorizationToken}
    +                                                         />
    +                                                         <span className=\"help-inline\">
    +                                                             Authentication token of a user or an integration
    +                                                         </span>
    +                                                     </div>
    +                                                 </div>
    +                                             ) : null}
    +                                             {hook.configuration.mode == 'WEBHOOK' &&
    +                                             !hook.configuration.useGlobalChannel ? (
    +                                                 <div className=\"control-group\">
    +                                                     <label className=\"control-label\">Webhook URL</label>
    +                                                     <div className=\"controls\">
    +                                                         <input type=\"text\" value={hook.configuration.webhookUrl} />
    +                                                     </div>
    +                                                 </div>
    +                                             ) : null}
    +                                             {!hook.configuration.useGlobalChannel ? (
    +                                                 <div className=\"control-group\">
    +                                                     <label className=\"control-label\">使用代理</label>
    +                                                     <div className=\"controls\">
    +                                                         <input type=\"checkbox\" value={hook.configuration.useProxy} />
    +                                                         <span className=\"help-inline\">使用 DSS 代理设置连接 </span>
    +                                                     </div>
    +                                                 </div>
    +                                             ) : null}
    +                                             {!hook.configuration.useGlobalChannel ? (
    +                                                 <div className=\"control-group\">
    +                                                     <label className=\"control-label\">通道</label>
    +                                                     <div className=\"controls\">
    +                                                         <input type=\"text\" value={hook.configuration.channel} />
    +                                                         <span className=\"help-inline\">一个 Slack 通道的Id</span>
    +                                                     </div>
    +                                                 </div>
    +                                             ) : null}
    + 
    +                                             {!hook.configuration.mode == 'API' ? (
    +                                                 <div className=\"control-group\">
    +                                                     <label className=\"control-label\">作为用户</label>
    +                                                     <div className=\"controls\">
    +                                                         <input type=\"checkbox\" value={hook.configuration.asUser} />
    +                                                         <span className=\"help-inline\">发送使用授权的 token 标识</span>
    +                                                     </div>
    +                                                 </div>
    +                                             ) : null}
    +                                             {!hook.configuration.asUser || hook.configuration.mode == 'WEBHOOK' ? (
    +                                                 <div className=\"control-group\">
    +                                                     <label className=\"control-label\">用户名称</label>
    +                                                     <div className=\"controls\">
    +                                                         <input type=\"text\" value={hook.configuration.username} />
    +                                                         <span className=\"help-inline\">
    +                                                             {' '}
    +                                                             The message will appear as sent by this user
    +                                                         </span>
    +                                                     </div>
    +                                                 </div>
    +                                             ) : null}
    +                                             <div className=\"control-group\">
    +                                                 <label className=\"control-label\">Icon (emoji)</label>
    +                                                 <div className=\"controls\">
    +                                                     <input type=\"text\" value={hook.configuration.iconEmoji} />
    +                                                     <span className=\"help-inline\">可选: emoji to use as icon</span>
    +                                                 </div>
    +                                             </div>
    +                                             <div className=\"control-group\">
    +                                                 <label className=\"control-label\">Icon (url)</label>
    +                                                 <div className=\"controls\">
    +                                                     <input type=\"text\" value={hook.configuration.iconUrl} />
    +                                                     <span className=\"help-inline\">可选: url to use as icon</span>
    +                                                 </div>
    +                                             </div>
    +                                             <div include-no-scope=\"/templates/projects/integrations/messaging-like-selection.html\" />
    +                                         </div>
    +                                     ) : null}
    +                                 </div>
    +                             ) : null}
    +                         </div>
    +                     ) : null}
    +                 </div>
    +             ) : null}
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;