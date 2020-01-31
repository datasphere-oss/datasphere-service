import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"fh\">
    +             <div block-api-error=\"\" />
    + 
    +             <div ng-switch=\"\" on=\"insight.params.filetype\">
    +                 <div ng-switch-when=\"html\" className=\"iframe-wrapper\">
    +                     <iframe
    +                         sandbox=\"allow-forms allow-pointer-lock allow-popups allow-scripts\"
    +                         className=\"insightHTMLContent\"
    +                         src={insightContentURL}>{`
    +         </div>
    +         <div ng-switch-when=\"image\" class=\"image-wrapper\" style=\"text-align: center\">
    +             <img ng-src=\"{{insightContentURL}}\" />
    +         </div>
    +         <div ng-switch-default style=\"font-weight: 300; font-size: 36px; text-align: center; margin-top: 60px\">
    +             <a href=\"\" class=\"link-std\" ng-click=\"download()\">
    +                 <i class=\"icon-download\" />
    +                 下载
    +             </a>
    +         </div>
    +     </div>
    + </div>`}</iframe>
    +                 </div>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;