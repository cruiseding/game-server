/*******************************************************************************
 * Copyright 2014 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.jzy.game.ai.btree;

import java.util.Collections;
import java.util.List;

import com.jzy.game.engine.math.MathUtil;

/**
 * 只能运行一个子任务的分支任务<br>
 * A {@code SingleRunningChildBranch} task is a branch task that supports only
 * one running child at a time.
 * 
 * @param <E>
 *            type of the blackboard object that tasks use to read or modify
 *            game state
 * 
 * @author implicit-invocation
 * @author davebaol
 * @fix CruiseDing
 */
public abstract class SingleRunningChildBranch<E> extends BranchTask<E> {

	/** The child in the running status or {@code null} if no child is running. */
	protected Task<E> runningChild;

	/** The index of the child currently processed. */
	protected int currentChildIndex;

	/**
	 * Array of random children. If it's {@code null} this task is deterministic.
	 */
	protected Task<E>[] randomChildren;

	/** Creates a {@code SingleRunningChildBranch} task with no children */
	public SingleRunningChildBranch() {
		super();
	}

	/**
	 * Creates a {@code SingleRunningChildBranch} task with a list of children
	 * 
	 * @param tasks
	 *            list of this task's children, can be empty
	 */
	public SingleRunningChildBranch(List<Task<E>> tasks) {
		super(tasks);
	}

	@Override
	public void childRunning(Task<E> task, Task<E> reporter) {
		runningChild = task;
		running(); // Return a running status when a child says it's running
	}

	@Override
	public void childSuccess(Task<E> task) {
		this.runningChild = null;
	}

	@Override
	public void childFail(Task<E> task) {
		this.runningChild = null;
	}

	@Override
	public void run() {
		if (runningChild != null) {
			runningChild.run();
		} else {
			if (currentChildIndex < children.size()) {
				if (randomChildren != null) {
					int last = children.size() - 1;
					if (currentChildIndex < last) {
						// Random swap
						int otherChildIndex = MathUtil.random(currentChildIndex, last);
						Task<E> tmp = randomChildren[currentChildIndex];
						randomChildren[currentChildIndex] = randomChildren[otherChildIndex];
						randomChildren[otherChildIndex] = tmp;
					}
					runningChild = randomChildren[currentChildIndex];
				} else {
					runningChild = children.get(currentChildIndex);
				}
				runningChild.setControl(this);
				runningChild.start();
				if (!runningChild.checkGuard(this))
					runningChild.fail();
				else
					run();
			} else {
				// Should never happen; this case must be handled by subclasses in childXXX
				// methods
			}
		}
	}

	@Override
	public void start() {
		this.currentChildIndex = 0;
		runningChild = null;
	}

	@Override
	protected void cancelRunningChildren(int startIndex) {
		super.cancelRunningChildren(startIndex);
		runningChild = null;
	}

	@Override
	public void resetTask() {
		super.resetTask();
		this.currentChildIndex = 0;
		this.runningChild = null;
		this.randomChildren = null;
	}


	@SuppressWarnings("unchecked")
	protected Task<E>[] createRandomChildren() {
		Task<E>[] rndChildren = new Task[children.size()];
		System.arraycopy(children.toArray(new Task[children.size()]), 0, rndChildren, 0, children.size());
		return rndChildren;
	}

	@Override
	public void reset() {
		this.currentChildIndex = 0;
		this.runningChild = null;
		this.randomChildren = null;
		super.reset();
	}

}
