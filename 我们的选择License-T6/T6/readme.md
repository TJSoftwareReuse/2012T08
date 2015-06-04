## COMPONENT: Lisence

### Version 1.0

__functions__
>* 每次接受一个请求，计数+1
>* 根据已收到请求消息数量和预设的License数值，判断是否可以继续提供服务
    -  请求数量 <= 预设数量， 返回True
    -  请求数量 > 预设数量， 返回False
    
__parameters__
>* TotalService: total lisence provided(initial with 10) 
>* UsedService: lisence used(initial with 0)

__methods__
>* public LisenceClass() -- construct method
>* public bool useLisence() -- use lisence method
    
### Version 2.0

__functions__
>* 每次接受一个请求，计数+1
>* 根据已收到请求消息数量和预设的License数值，判断是否可以继续提供服务
    - 请求数量 <= 预设数量， 返回True
    - 请求数量 > 预设数量， 返回False
>* __(new function)__ 当Lisence数量更改时，重新开始从0计数

__parameters__
>* TotalService: total lisence provided(initial with 10) 
>* UsedService: lisence used(initial with 0)

__methods__
>* public LisenceClass() -- construct method
>* public bool useLisence() -- use lisence method
>* __(new method)__ public void resetLisenceNum(int newNum) -- reset lisence number to __newNum__ and reset __UsedLicense__ to 0
>* __(query interface)__ public int totalService() -- return total services can provided currently
>* __(query interface)__ public int usedService() -- return used services currently
>* __(query interface)__ public int restService() -- return services rest for use currently