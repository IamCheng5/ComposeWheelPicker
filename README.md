# ComposeWheelPicker
Step1. Add it in your root build.gradle at the end of repositories:

	allprojects {
        repositories {
          ...
          maven { url 'https://jitpack.io' }
        }
	}
Step 2. Add the dependency

	dependencies {
        implementation 'com.github.IamCheng5:ComposeWheelPicker:1.1'
	}
## Feature
### VerticalWheelPicker
    @Composable
    fun VerticalWheelPicker(
        modifier: Modifier = Modifier,
        count: Int,
        state: LazyListState = rememberLazyListState(),
        itemHeight: Dp,
        visibleItemCount: Int,
        onScrollFinish: (index: Int) -> Unit,
        content: @Composable BoxScope.(index: Int) -> Unit
    )
### HorizontalWheelPicker
	@Composable
	fun HorizontalWheelPicker(
	    modifier: Modifier = Modifier,
	    count: Int,
	    state: LazyListState = rememberLazyListState(),
	    itemWidth: Dp,
	    visibleItemCount: Int,
	    onScrollFinish: (index: Int) -> Unit,
	    content: @Composable BoxScope.(index: Int) -> Unit
	)
## Sample
![image](https://user-images.githubusercontent.com/29422378/201000912-1b6c0367-0115-4b62-9ff2-4ea9f472e2a4.png)

      @Composable
      internal fun TextPicker() {
          val startIndex = 2
          val state = rememberLazyListState(startIndex)
          val scope = rememberCoroutineScope()
          var currentIndex by remember { mutableStateOf(startIndex) }
          VerticalWheelPicker(
              state = state,
              count = 10,
              itemHeight = 44.dp,
              visibleItemCount = 3,
              onScrollFinish = { currentIndex = it }) { index ->
              Box(
                  modifier = Modifier
                      .fillMaxHeight()
                      .clickable { scope.launch { state.animateScrollToItem(index) } },
                  contentAlignment = Alignment.Center
              ) {
                  Text(
                      text = "Text $index",
                      color = if (index == currentIndex) Color.Black else Color.Gray
                  )
              }
          }
      }
    
![image](https://user-images.githubusercontent.com/29422378/201001249-70bcf057-8018-4049-b82f-9e651fdede4b.png)


      @Composable
      internal fun LoopingTextPicker() {
          val startIndex = 1
          val count = 10
          val realStartIndex = getLoopingStartIndex(startIndex, count)
          val state = rememberLazyListState(realStartIndex)
          val scope = rememberCoroutineScope()
          var currentIndex by remember { mutableStateOf(realStartIndex) }
          VerticalWheelPicker(
              state = state,
              count = loopingRealCount,
              itemHeight = 44.dp,
              visibleItemCount = 5,
              onScrollFinish = { currentIndex = it }) { index ->
              Box(
                  modifier = Modifier
                      .fillMaxHeight()
                      .clickable { scope.launch { state.animateScrollToItem(index) } },
                  contentAlignment = Alignment.Center
              ) {
                  Text(
                      text = "Text ${index % count}",
                      color = if (index == currentIndex) Color.Black else Color.Gray
                  )
              }
          }
      }

## Showcase
 

https://user-images.githubusercontent.com/29422378/200999644-ba409fa0-dbef-4e2b-9017-6ae92f24553e.mp4

