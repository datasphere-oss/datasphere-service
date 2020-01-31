import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return [
    +         !$root.appConfig.admin && error.fixability == 'THIRD_PARTY_SYSTEM_CONNECTION' ? (
    +             <p key=\"child-0\">
    +                 这个错误 <strong>通过一个连接问题被导致在第三方系统和DSS平台之间 </strong>.
    +                 您的管理员需要进行干预以解决问题.{' '}
    +                 {$root.appConfig.studioAdminContact ? (
    +                     <a
    +                         onClick={() => {
    +                             $root.showAdminContactInfo();
    +                         }}>
    +                         怎么联系管理员?
    +                     </a>
    +                 ) : null}
    +             </p>
    +         ) : null,
    + 
    +         $root.appConfig.admin && error.fixability == 'THIRD_PARTY_SYSTEM_CONNECTION' ? (
    +             <p key=\"child-2\">
    +                 这个错误 <strong>通过一个连接问题被导致在第三方系统和DSS平台之间 </strong>.
    +                 需要检查连接设置和第三方系统的运行状况
    +             </p>
    +         ) : null,
    +         !$root.appConfig.admin && error.fixability == 'ADMIN_SETTINGS_CONNECTIONS' ? (
    +             <p key=\"child-3\">
    +                 这个错误 <strong>在连接中通过配置问题来导致 </strong> (在管理员设置中).
    +                 需要检查连接设置和第三方系统的运行状况.{' '}
    +                 {$root.appConfig.studioAdminContact ? (
    +                     <a
    +                         onClick={() => {
    +                             $root.showAdminContactInfo();
    +                         }}>
    +                         怎么联系管理员?
    +                     </a>
    +                 ) : null}
    +             </p>
    +         ) : null,
    +         $root.appConfig.admin && error.fixability == 'ADMIN_SETTINGS_CONNECTIONS' ? (
    +             <p key=\"child-5\">
    +                 这个错误 <strong>在连接中通过配置问题来导致</strong> (在管理员设置中). 需要 检查连接设置
    +             </p>
    +         ) : null,
    + 
    +         !$root.appConfig.admin && error.fixability == 'ADMIN_SETTINGS_SECURITY' ? (
    +             <p key=\"child-7\">
    +                 这个错误 <strong>在全局安全设置中通过配置问题来导致 </strong> (在管理员设置中).
    +                 需要检查连接设置和第三方系统的运行状况.{' '}
    +                 {$root.appConfig.studioAdminContact ? (
    +                     <a
    +                         onClick={() => {
    +                             $root.showAdminContactInfo();
    +                         }}>
    +                         怎么联系管理员?
    +                     </a>
    +                 ) : null}
    +             </p>
    +         ) : null,
    +         $root.appConfig.admin && error.fixability == 'ADMIN_SETTINGS_SECURITY' ? (
    +             <p key=\"child-9\">
    +                 这个错误 <strong>在全局安全设置中通过配置问题来导致 </strong> (在管理员设置中). 需要检查连接设置
    +             </p>
    +         ) : null,
    + 
    +         !$root.appConfig.admin && error.fixability == 'ADMIN_SETTINGS_CONTAINERS' ? (
    +             <p key=\"child-11\">
    +                 这个错误 <strong>在容器设置中通过配置问题来导致 </strong> (在管理员设置中).
    +                 需要检查连接设置和第三方系统的运行状况.{' '}
    +                 {$root.appConfig.studioAdminContact ? (
    +                     <a
    +                         onClick={() => {
    +                             $root.showAdminContactInfo();
    +                         }}>
    +                         怎么联系管理员?
    +                     </a>
    +                 ) : null}
    +             </p>
    +         ) : null,
    +         $root.appConfig.admin && error.fixability == 'ADMIN_SETTINGS_CONTAINERS' ? (
    +             <p key=\"child-13\">
    +                 这个错误 <strong>在容器设置中通过配置问题来导致 </strong> (在管理员设置中). 需要检查容器配置
    +             </p>
    +         ) : null,
    + 
    +         !$root.appConfig.admin && error.fixability == 'ADMIN_SETTINGS_CODEENVS' ? (
    +             <p key=\"child-15\">
    +                 这个错误 <strong>在代码环境中通过配置问题来导致 </strong> (在管理员设置中).
    +                 需要检查连接设置和第三方系统的运行状况.{' '}
    +                 {$root.appConfig.studioAdminContact ? (
    +                     <a
    +                         onClick={() => {
    +                             $root.showAdminContactInfo();
    +                         }}>
    +                         怎么联系管理员?
    +                     </a>
    +                 ) : null}
    +             </p>
    +         ) : null,
    + 
    +         $root.appConfig.admin && error.fixability == 'ADMIN_SETTINGS_CODEENVS' ? (
    +             <p key=\"child-17\">
    +                 这个错误 <strong>在代码环境中通过配置问题来导致 </strong> (在管理员设置中). 需要检查代码环境设置
    +             </p>
    +         ) : null,
    + 
    +         error.fixability == 'ADMIN_SETTINGS_USER_PROFILE' ? (
    +             <p key=\"child-19\">
    +                 这个错误 <strong>需要管理员的配置 </strong>. 管理员需要更新你的用户资料.
    +             </p>
    +         ) : null,
    + 
    +         error.fixability == 'USER_CONFIG_DATASET' ? (
    +             <p key=\"child-21\">
    +                 这个错误 <strong>在数据集设置中通过配置问题来导致 </strong>. 需要修改受影响的数据集设置修复此问题
    +             </p>
    +         ) : null,
    + 
    +         error.fixability == 'USER_CONFIG' ? (
    +             <p key=\"child-23\">
    +                 这个错误 <strong>通过配置问题来导致 </strong>. 需要修改受影响的数据集设置修复此问题
    +             </p>
    +         ) : null,
    + 
    +         error.fixability == 'USER_CONFIG_OR_BUILD' ? (
    +             <div key=\"child-25\">
    +                 <p style=\"margin-bottom: 0\">这个错误通过另外的错误导致:</p>
    +                 <ul>
    +                     <li>
    +                         <strong>一个数据集配置错误</strong>. 需要修改受影响的数据集设置修复此问题
    +                     </li>
    +                     <li>
    +                         <strong>数据集需要被重构 </strong> (如果数据流中它是目标端).
    +                     </li>
    +                 </ul>
    +             </div>
    +         ) : null,
    + 
    +         error.fixability == 'READ_FUTURE_LOG' ? <p key=\"child-27\">请读取此错误更详细的日志</p> : null,
    + 
    +         error.fixability == 'DATA' ? (
    +             <p key=\"child-29\">
    +                 这个错误 <strong>通过数据的错误导致 </strong> (i.e. 数据是无效的). 如果数据通过DSS被创建, 你可能需要{' '}
    +                 <strong>重构它</strong>.
    +             </p>
    +         ) : null,
    + 
    +         !$root.appConfig.admin && error.fixability == 'ADMIN_INSTALLATION' ? (
    +             <p key=\"child-31\">
    +                 这个错误 <strong>通过DSS安装问题导致 </strong>. 需要修改受影响的设置修复此问题.{' '}
    +                 {$root.appConfig.studioAdminContact ? (
    +                     <a
    +                         onClick={() => {
    +                             $root.showAdminContactInfo();
    +                         }}>
    +                         如何联系你的管理员?
    +                     </a>
    +                 ) : null}
    +             </p>
    +         ) : null,
    + 
    +         $root.appConfig.admin && error.fixability == 'ADMIN_INSTALLATION' ? (
    +             <p key=\"child-33\">
    +                 这个错误 <strong>通过DSS安装问题导致</strong>.
    +             </p>
    +         ) : null,
    + 
    +         !$root.appConfig.admin && error.fixability == 'ADMIN_TROUBLESHOOTING' ? (
    +             <p key=\"child-35\">
    +                 这个错误 <strong>需要管理员排查 </strong>. 需要修改受影响的设置修复此问题.{' '}
    +                 {$root.appConfig.studioAdminContact ? (
    +                     <a
    +                         onClick={() => {
    +                             $root.showAdminContactInfo();
    +                         }}>
    +                         如何联系你的管理员?
    +                     </a>
    +                 ) : null}
    +             </p>
    +         ) : null,
    + 
    +         $root.appConfig.admin && error.fixability == 'ADMIN_TROUBLESHOOTING' ? (
    +             <p key=\"child-37\">
    +                 这个错误 <strong>需要管理员排查</strong>. 请参考如下文档.
    +             </p>
    +         ) : null
    +     ];
    + };
    + 
    + export default TestComponent;