package org.bubbble.andsplash.di

import androidx.recyclerview.widget.RecyclerView
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Named

/**
 * @author Andrew
 * @date 2020/10/26 21:55
 */
@InstallIn(FragmentComponent::class)
@Module
class FragmentModule {

    @FragmentScoped
    @Provides
    @Named("tagViewPool")
    fun providesTagViewPool(): RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()
}