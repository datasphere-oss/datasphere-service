import * as React from 'react';
    
    export interface TestComponentProps {
        [key: string]: any;
    }
    
    const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
        return (
            <div className=\"modal modal3 dku-modal modal-w800\">
                <div dku-modal-header-with-totem=\"\" modal-title=\"组件和脚本之间不匹配\" modal-totem=\"icon-warning-sign\">
                    <div className=\"modal-body\">
                        <p>此组件有一个关联的notebook 但是 notebook 内容不再匹配组件代码:</p>
                        <ul>
                            <li>
                                自上次从脚本中保存以来，您编辑了组件的代码. 选择 \"覆盖脚本内容 \"
                                来替换脚本的内容通过当前的组件内容.
                            </li>
                            <li>
                                您编辑了脚本中的代码，但未将其保存回组件中. 选择 \\\"查看当前脚本 \\\" 转到查看脚本,
                                不需要改变其内容.
                            </li>
                        </ul>
                    </div>
                    <div className=\"modal-footer modal-footer-std-buttons\" global-keydown=\"{ 'esc': 'cancel();' }\">
                        <div className=\"pull-right\">
                            <button
                                className=\"btn btn-danger\"
                                onClick={() => {
                                    erase();
                                }}>
                                <i className=\"icon-hdd\" />
                                覆盖脚本内容
                            </button>
    
                            <button
                                className=\"btn btn-primary\"
                                onClick={() => {
                                    forget();
                                }}>
                                <i className=\"icon-share-alt\" />
                                查看当前脚本
                            </button>
    
                            <button
                                className=\"btn btn-default\"
                                onClick={() => {
                                    cancel();
                                }}>
                                <i className=\"icon-remove\" />
                                取消
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        );
    };
    
    export default TestComponent;