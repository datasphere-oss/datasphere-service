import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"boxed-next-to-sidebar\">
    +             <div>
    +                 <form
    +                     name=\"usersettingsForm\"
    +                     className=\"dkuform-horizontal\"
    +                     global-keydown=\"{'ctrl-s meta-s':'saveUserSettings()'}\">
    +                     <h1 className=\"page-title\">我的设置</h1>
    + 
    +                     <h2 className=\"settings-section-title\">消息通知</h2>
    +                     <div className=\"helper\">
    +                         这些通知显示在屏幕的右下角 (<a
    +                             onClick={() => {
    +                                 sampleMessengerNotification();
    +                             }}>
    +                             示例
    +                         </a>)
    +                     </div>
    +                     <div className=\"control-group\">
    +                         <label htmlFor=\"\" className=\"control-label\">
    +                             显示通知{' '}
    +                         </label>
    +                         <div className=\"controls\">
    +                             <label>
    +                                 <input type=\"checkbox\" value={userSettings.frontendNotifications.loginLogout} />
    +                                 其他用户登录/退出
    +                             </label>
    +                             <label>
    +                                 <input
    +                                     type=\"checkbox\"
    +                                     value={userSettings.frontendNotifications.watchedObjectsEditions}
    +                                 />
    +                                 我正在观看的对象已被编辑
    +                             </label>
    +                             <label>
    +                                 <input
    +                                     type=\"checkbox\"
    +                                     value={userSettings.frontendNotifications.objectOnProjectCreatedDeleted}
    +                                 />
    +                                 在我正在浏览的项目上创建/删除对象
    +                             </label>
    +                             <label>
    +                                 <input
    +                                     type=\"checkbox\"
    +                                     value={userSettings.frontendNotifications.anyObjectOnProjectEdited}
    +                                 />
    +                                 在我正在浏览的项目上编辑任何对象
    +                             </label>
    +                             <label>
    +                                 <input type=\"checkbox\" value={userSettings.frontendNotifications.watchStar} />
    +                                 我正在浏览的项目上观看或加注了一个对象
    +                             </label>
    +                             <label>
    +                                 <input type=\"checkbox\" value={userSettings.frontendNotifications.otherUsersTasks} />
    +                                 其他用户运行作业/方案/ ML任务
    +                             </label>
    +                         </div>
    +                     </div>
    + 
    +                     <h2 className=\"settings-section-title\" style=\"line-height: 27px;\">
    +                         <label
    +                             className=\"{error: userSettings.mentionEmails.enabled &amp;&amp; !profile.user.email}\"
    +                             title={
    +                                 userSettings.mentionEmails.enabled && !profile.user.email
    +                                     ? 'Your email address is required, fill it in the account tab'
    +                                     : ''
    +                             }>
    +                             <input type=\"checkbox\" value={userSettings.mentionEmails.enabled} />
    +                             <span />
    +                         </label>
    +                         我被提到时给我发电子邮件
    +                     </h2>
    +                     <div className=\"helper\">
    +                         其他用户可以在讨论或提交消息时提及您, 使用 \"<span style=\"font-style:italic\">
    +                             @{profile.user.login}
    +                         </span>\" 您可以随时激活电子邮件.
    +                     </div>
    +                     {userSettings.mentionEmails.enabled && !profile.user.email ? (
    +                         <div className=\"alert alert-error\">
    +                             您需要填写您的电子邮件地址,在 <NavLink to=\"profile.my.account\"> 账户页面 </NavLink>
    +                         </div>
    +                     ) : null}
    + 
    +                     <h2 className=\"settings-section-title\" style=\"line-height: 27px;\">
    +                         <label
    +                             className=\"{error: userSettings.discussionEmails.enabled &amp;&amp; !profile.user.email}\"
    +                             title={
    +                                 userSettings.discussionEmails.enabled && !profile.user.email
    +                                     ? 'Your email address is required, fill it in the account tab'
    +                                     : ''
    +                             }>
    +                             <input type=\"checkbox\" value={userSettings.discussionEmails.enabled} />
    +                             <span />
    +                         </label>
    +                         用户评论我正在观看的对象时给我发电子邮件
    +                     </h2>
    +                     <div className=\"helper\">
    +                         只要用户在您正在观看的任何项目的讨论主题中写入，启用此选项就会向您发送电子邮件.
    +                     </div>
    +                     {userSettings.discussionEmails.enabled && !profile.user.email ? (
    +                         <div className=\"alert alert-error\">
    +                             您需要填写您的电子邮件地址, 在 <NavLink to=\"profile.my.account\"> 账户页面 </NavLink>
    +                         </div>
    +                     ) : null}
    + 
    +                     <h2 className=\"settings-section-title\" style=\"line-height: 27px;\">
    +                         <label
    +                             className=\"{error: userSettings.offlineQueue.enabled &amp;&amp; !profile.user.email}\"
    +                             title={
    +                                 userSettings.offlineQueue.enabled && !profile.user.email
    +                                     ? 'Your email address is required, fill it in the account tab'
    +                                     : ''
    +                             }>
    +                             <input type=\"checkbox\" value={userSettings.offlineQueue.enabled} />
    +                             <span />
    +                         </label>
    +                         离线活动电子邮件
    +                     </h2>
    +                     <div className=\"helper\">
    +                         DSS可以向您发送电子邮件，以便在您离线时定期通知您有关其活动的信息. <br />
    +                     </div>
    +                     {userSettings.offlineQueue.enabled && !profile.user.email ? (
    +                         <div className=\"alert alert-error\">
    +                             您需要填写您的电子邮件地址, 在 <NavLink to=\"profile.my.account\"> 账户页面 </NavLink>
    +                         </div>
    +                     ) : null}
    + 
    +                     <h2 className=\"settings-section-title\" style=\"line-height: 27px;\">
    +                         <label
    +                             className=\"{error: userSettings.digests.enabled &amp;&amp; !profile.user.email}\"
    +                             title={
    +                                 userSettings.digests.enabled && !profile.user.email
    +                                     ? 'Your email address is required, fill it in the account tab'
    +                                     : ''
    +                             }>
    +                             <input type=\"checkbox\" value={userSettings.digests.enabled} />
    +                             <span />
    +                         </label>
    +                         每日摘要
    +                     </h2>
    +                     <div className=\"helper\">无论您是否登录，DSS都可以向您发送电子邮件以总结当天的活动。</div>
    +                     {userSettings.digests.enabled && !profile.user.email ? (
    +                         <div className=\"alert alert-error\">
    +                             您需要填写您的电子邮件地址, 在 <NavLink to=\"profile.my.account\"> 账户页面 </NavLink>
    +                         </div>
    +                     ) : null}
    + 
    +                     {/* Code Editor */}
    + 
    +                     <h2 className=\"settings-section-title\">代码编辑器</h2>
    + 
    +                     <div className=\"control-group\">
    +                         <label className=\"control-label\">自动关闭括号</label>
    +                         <div className=\"controls\">
    +                             <input type=\"checkbox\" value={userSettings.codeEditor.autoCloseBrackets} />
    +                         </div>
    +                     </div>
    + 
    +                     <div className=\"control-group\">
    +                         <label className=\"control-label\">自动关闭标签</label>
    +                         <div className=\"controls\">
    +                             <input type=\"checkbox\" value={userSettings.codeEditor.autoCloseTags} />
    +                         </div>
    +                     </div>
    + 
    +                     <div className=\"control-group\">
    +                         <label className=\"control-label\">缩进单位</label>
    +                         <div className=\"controls\">
    +                             <select
    +                                 dku-bs-select=\"\"
    +                                 value={userSettings.codeEditor.indentUnit}
    +                                 ng-options=\"indentUnit for indentUnit in [1, 2, 3, 4]\"
    +                             />
    +                         </div>
    +                     </div>
    + 
    +                     <div className=\"control-group\">
    +                         <label className=\"control-label\">页面大小</label>
    +                         <div className=\"controls\">
    +                             <select
    +                                 dku-bs-select=\"\"
    +                                 value={userSettings.codeEditor.tabSize}
    +                                 ng-options=\"tabSize for tabSize in [1, 2, 3, 4]\"
    +                             />
    +                         </div>
    +                     </div>
    + 
    +                     <div className=\"control-group\">
    +                         <label className=\"control-label\">缩进标签</label>
    +                         <div className=\"controls\">
    +                             <input type=\"checkbox\" value={userSettings.codeEditor.indentWithTabs} />
    +                         </div>
    +                     </div>
    + 
    +                     <div className=\"control-group\">
    +                         <label className=\"control-label\">按键</label>
    +                         <div className=\"controls\">
    +                             <select
    +                                 dku-bs-select=\"\"
    +                                 value={userSettings.codeEditor.keyMap}
    +                                 ng-options=\"keymap for keymap in ['default', 'emacs', 'vim', 'sublime']\"
    +                                 onChange={() => {
    +                                     updatePreview();
    +                                 }}
    +                             />
    +                         </div>
    +                     </div>
    + 
    +                     <div className=\"control-group\">
    +                         <label className=\"control-label\">主题</label>
    +                         <div className=\"controls\">
    +                             <select
    +                                 dku-bs-select=\"\"
    +                                 value={userSettings.codeEditor.theme}
    +                                 ng-options=\"theme for theme in codeMirrorThemeList\"
    +                                 onChange={() => {
    +                                     updatePreview();
    +                                 }}
    +                             />
    +                         </div>
    +                     </div>
    + 
    +                     <div className=\"control-group\">
    +                         <label className=\"control-label\">字体大小</label>
    +                         <div className=\"controls\">
    +                             <select
    +                                 dku-bs-select=\"\"
    +                                 value={userSettings.codeEditor.fontSize}
    +                                 ng-options=\"fontSize for fontSize in fontSizes\"
    +                                 onChange={() => {
    +                                     updatePreview();
    +                                 }}
    +                             />
    +                         </div>
    +                     </div>
    + 
    +                     <div className=\"control-group\">
    +                         <label className=\"control-label\">预览</label>
    +                         <div className=\"controls\">
    +                             <textarea value={sample} ui-codemirror=\"editorOptions\" ui-refresh=\"reflow\" />
    +                         </div>
    +                     </div>
    + 
    +                     <h2 className=\"settings-section-title\" style=\"line-height: 27px;\">
    +                         <label className=\"dku-toggle\">
    +                             <input
    +                                 type=\"checkbox\"
    +                                 value={isEnableFlowZoomTracking}
    +                                 ng-model-options={{getterSetter: true}}
    +                             />
    +                             <span />
    +                         </label>
    +                         记住你在流程中的位置
    +                     </h2>
    +                     <div className=\"helper\">
    +                         默认情况下，流程会记住您的缩放设置，并会在您返回时自动重新选择您查看的最后一项.
    +                     </div>
    +                 </form>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;